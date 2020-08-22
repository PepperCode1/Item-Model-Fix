package me.pepperbell.itemmodelfix.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.pepperbell.itemmodelfix.ItemModelFix;
import me.pepperbell.itemmodelfix.util.MathUtil;
import me.pepperbell.itemmodelfix.util.ModelGenerationType;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelElementTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
	@Inject(at=@At(value="TAIL"),method="addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/Sprite;)Ljava/util/List;",locals=LocalCapture.CAPTURE_FAILHARD)
	private void onAddLayerElements(int layer, String key, Sprite sprite, CallbackInfoReturnable<List<ModelElement>> cir, Map<Direction, ModelElementFace> map, List<ModelElement> list) {
		if (ItemModelFix.getConfig().getOptions().generationType == ModelGenerationType.UNLERP) {
			for (ModelElement element : list) {
				for (ModelElementFace face : element.faces.values()) {
					MathUtil.unlerpUVs(face.textureData.uvs, sprite);
				}
			}
		}
	}
	
	@Inject(at=@At(value="HEAD"),method="addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/Sprite;)Ljava/util/List;",cancellable=true)
	public void onAddLayerElements1(int layer, String key, Sprite sprite, CallbackInfoReturnable<List<ModelElement>> cir) {
		if (ItemModelFix.getConfig().getOptions().generationType == ModelGenerationType.PIXEL) {
			cir.setReturnValue(addLayerElementsPixels(layer, key, sprite));
		}
	}
	
	private List<ModelElement> addLayerElementsPixels(int layer, String key, Sprite sprite) {
		List<ModelElement> elements = Lists.newArrayList();
		
		int width = sprite.getWidth();
		int height = sprite.getHeight();
		float xRatio = width/16;
		float yRatio = height/16;
		
	    for (int f = 0; f < sprite.getFrameCount(); ++f) {
	    	for (int y = 0; y < height; ++y) {
				for (int x = 0; x < width; ++x) {
					if (!isPixelTransparent(sprite, f, x, y)) {
						Map<Direction, ModelElementFace> map = Maps.newHashMap();
						ModelElementFace face = new ModelElementFace(null, layer, key, new ModelElementTexture(new float[]{x/xRatio, y/yRatio, (x+1)/xRatio, (y+1)/yRatio}, 0));
						map.put(Direction.NORTH, face);
						map.put(Direction.SOUTH, face);
						if (isPixelTransparent(sprite, f, x+1, y)) {
							map.put(Direction.EAST, face);
						}
						if (isPixelTransparent(sprite, f, x-1, y)) {
							map.put(Direction.WEST, face);
						}
						if (isPixelTransparent(sprite, f, x, y+1)) {
							map.put(Direction.DOWN, face);
						}
						if (isPixelTransparent(sprite, f, x, y-1)) {
							map.put(Direction.UP, face);
						}
						elements.add(new ModelElement(new Vector3f(x/xRatio, (height-(y+1))/yRatio, 7.5F), new Vector3f((x+1)/xRatio, (height-y)/yRatio, 8.5f), map, null, true));
					}
				}
	    	}
	    }
	    
		return elements;
	}

	private static boolean isPixelTransparent(Sprite sprite, int frame, int x, int y) {
		return (x<0||y<0||x>=sprite.getWidth()||y>=sprite.getHeight()) ? true : sprite.isPixelTransparent(frame, x, y);
	}
}
