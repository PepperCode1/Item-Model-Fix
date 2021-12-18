package me.pepperbell.itemmodelfix.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import me.pepperbell.itemmodelfix.util.MathUtil;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelElementTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class ItemModelUtil {
	public static void unlerpElements(List<ModelElement> elements, float delta) {
		for (ModelElement element : elements) {
			for (ModelElementFace face : element.faces.values()) {
				MathUtil.unlerpUVs(face.textureData.uvs, delta);
			}
		}
	}

	public static List<ModelElement> createOutlineLayerElements(int layer, String key, Sprite sprite) {
		List<ModelElement> elements = new ArrayList<>();

		int width = sprite.getWidth();
		int height = sprite.getHeight();
		float xFactor = width / 16.0F;
		float yFactor = height / 16.0F;
		float animationFrameDelta = sprite.getAnimationFrameDelta();
		int[] frames = sprite.getDistinctFrameCount().toArray();

		Map<Direction, ModelElementFace> map = new EnumMap<>(Direction.class);
		map.put(Direction.SOUTH, new ModelElementFace(null, layer, key, createUnlerpedTexture(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, 0, animationFrameDelta)));
		map.put(Direction.NORTH, new ModelElementFace(null, layer, key, createUnlerpedTexture(new float[] { 16.0F, 0.0F, 0.0F, 16.0F }, 0, animationFrameDelta)));
		elements.add(new ModelElement(new Vec3f(0.0F, 0.0F, 7.5F), new Vec3f(16.0F, 16.0F, 8.5F), map, null, true));

		int first1 = -1;
		int first2 = -1;
		int last1 = -1;
		int last2 = -1;

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
					if (doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.DOWN)) {
						if (first1 == -1) {
							first1 = x;
						}
						last1 = x;
					}
					if (doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.UP)) {
						if (first2 == -1) {
							first2 = x;
						}
						last2 = x;
					}
				} else {
					if (first1 != -1) {
						elements.add(createHorizontalOutlineElement(Direction.DOWN, layer, key, first1, last1, y, height, animationFrameDelta, xFactor, yFactor));
						first1 = -1;
					}
					if (first2 != -1) {
						elements.add(createHorizontalOutlineElement(Direction.UP, layer, key, first2, last2, y, height, animationFrameDelta, xFactor, yFactor));
						first2 = -1;
					}
				}
			}

			if (first1 != -1) {
				elements.add(createHorizontalOutlineElement(Direction.DOWN, layer, key, first1, last1, y, height, animationFrameDelta, xFactor, yFactor));
				first1 = -1;
			}
			if (first2 != -1) {
				elements.add(createHorizontalOutlineElement(Direction.UP, layer, key, first2, last2, y, height, animationFrameDelta, xFactor, yFactor));
				first2 = -1;
			}
		}

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
					if (doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.RIGHT)) {
						if (first1 == -1) {
							first1 = y;
						}
						last1 = y;
					}
					if (doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.LEFT)) {
						if (first2 == -1) {
							first2 = y;
						}
						last2 = y;
					}
				} else {
					if (first1 != -1) {
						elements.add(createVerticalOutlineElement(Direction.EAST, layer, key, first1, last1, x, height, animationFrameDelta, xFactor, yFactor));
						first1 = -1;
					}
					if (first2 != -1) {
						elements.add(createVerticalOutlineElement(Direction.WEST, layer, key, first2, last2, x, height, animationFrameDelta, xFactor, yFactor));
						first2 = -1;
					}
				}
			}

			if (first1 != -1) {
				elements.add(createVerticalOutlineElement(Direction.EAST, layer, key, first1, last1, x, height, animationFrameDelta, xFactor, yFactor));
				first1 = -1;
			}
			if (first2 != -1) {
				elements.add(createVerticalOutlineElement(Direction.WEST, layer, key, first2, last2, x, height, animationFrameDelta, xFactor, yFactor));
				first2 = -1;
			}
		}

		return elements;
	}

	public static ModelElement createHorizontalOutlineElement(Direction direction, int layer, String key, int start, int end, int y, int height, float animationFrameDelta, float xFactor, float yFactor) {
		Map<Direction, ModelElementFace> faces = new EnumMap<>(Direction.class);
		faces.put(direction, new ModelElementFace(null, layer, key, createUnlerpedTexture(new float[] { start / xFactor, y / yFactor, (end + 1) / xFactor, (y + 1) / yFactor }, 0, animationFrameDelta)));
		return new ModelElement(new Vec3f(start / xFactor, (height - (y + 1)) / yFactor, 7.5F), new Vec3f((end + 1) / xFactor, (height - y) / yFactor, 8.5F), faces, null, true);
	}

	public static ModelElement createVerticalOutlineElement(Direction direction, int layer, String key, int start, int end, int x, int height, float animationFrameDelta, float xFactor, float yFactor) {
		Map<Direction, ModelElementFace> faces = new EnumMap<>(Direction.class);
		faces.put(direction, new ModelElementFace(null, layer, key, createUnlerpedTexture(new float[] { (x + 1) / xFactor, start / yFactor, x / xFactor, (end + 1) / yFactor }, 0, animationFrameDelta)));
		return new ModelElement(new Vec3f(x / xFactor, (height - (end + 1)) / yFactor, 7.5F), new Vec3f((x + 1) / xFactor, (height - start) / yFactor, 8.5F), faces, null, true);
	}

	public static ModelElementTexture createUnlerpedTexture(float[] uvs, int rotation, float delta) {
		return new ModelElementTexture(MathUtil.unlerpUVs(uvs, delta), rotation);
	}

	public static List<ModelElement> createPixelLayerElements(int layer, String key, Sprite sprite) {
		List<ModelElement> elements = new ArrayList<>();

		int width = sprite.getWidth();
		int height = sprite.getHeight();
		float xFactor = width / 16.0F;
		float yFactor = height / 16.0F;
		int[] frames = sprite.getDistinctFrameCount().toArray();

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
					Map<Direction, ModelElementFace> faces = new EnumMap<>(Direction.class);
					ModelElementFace face = new ModelElementFace(null, layer, key, new ModelElementTexture(new float[] { x / xFactor, y / yFactor, (x + 1) / xFactor, (y + 1) / yFactor }, 0));
					ModelElementFace flippedFace = new ModelElementFace(null, layer, key, new ModelElementTexture(new float[] { (x + 1) / xFactor, y / yFactor, x / xFactor, (y + 1) / yFactor }, 0));

					faces.put(Direction.SOUTH, face);
					faces.put(Direction.NORTH, flippedFace);
					for (PixelDirection pixelDirection : PixelDirection.VALUES) {
						if (doesPixelHaveEdge(sprite, frames, x, y, pixelDirection)) {
							faces.put(pixelDirection.getDirection(), pixelDirection.isVertical() ? face : flippedFace);
						}
					}

					elements.add(new ModelElement(new Vec3f(x / xFactor, (height - (y + 1)) / yFactor, 7.5F), new Vec3f((x + 1) / xFactor, (height - y) / yFactor, 8.5F), faces, null, true));
				}
			}
		}

		return elements;
	}

	public static boolean isPixelOutsideSprite(Sprite sprite, int x, int y) {
		return x < 0 || y < 0 || x >= sprite.getWidth() || y >= sprite.getHeight();
	}

	public static boolean isPixelTransparent(Sprite sprite, int frame, int x, int y) {
		return isPixelOutsideSprite(sprite, x, y) ? true : sprite.isPixelTransparent(frame, x, y);
	}

	public static boolean isPixelAlwaysTransparent(Sprite sprite, int[] frames, int x, int y) {
		for (int frame : frames) {
			if (!isPixelTransparent(sprite, frame, x, y)) {
				return false;
			}
		}
		return true;
	}

	public static boolean doesPixelHaveEdge(Sprite sprite, int[] frames, int x, int y, PixelDirection direction) {
		int x1 = x + direction.getOffsetX();
		int y1 = y + direction.getOffsetY();
		if (isPixelOutsideSprite(sprite, x1, y1)) {
			return true;
		}
		for (int frame : frames) {
			if (!isPixelTransparent(sprite, frame, x, y) && isPixelTransparent(sprite, frame, x1, y1)) {
				return true;
			}
		}
		return false;
	}
}
