package me.pepperbell.itemmodelfix.util;

import net.minecraft.client.texture.Sprite;

public class MathUtil {
	public static float unlerp(float delta, float start, float end) {
		return (start - delta * end) / (1 - delta);
	}

	public static float[] unlerpUVs(float[] uvs, float delta) {
		float centerU = (uvs[0] + uvs[2]) / 2.0F;
		float centerV = (uvs[1] + uvs[3]) / 2.0F;
		uvs[0] = unlerp(delta, uvs[0], centerU);
		uvs[2] = unlerp(delta, uvs[2], centerU);
		uvs[1] = unlerp(delta, uvs[1], centerV);
		uvs[3] = unlerp(delta, uvs[3], centerV);
		return uvs;
	}

	public static float[] unlerpUVs(float[] uvs, Sprite sprite) {
		return unlerpUVs(uvs, sprite.getAnimationFrameDelta());
	}
}
