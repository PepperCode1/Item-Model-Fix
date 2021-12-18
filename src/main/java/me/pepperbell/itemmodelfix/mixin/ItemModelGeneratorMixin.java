package me.pepperbell.itemmodelfix.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import me.pepperbell.itemmodelfix.ItemModelFix;
import me.pepperbell.itemmodelfix.model.ItemModelGenerationType;
import me.pepperbell.itemmodelfix.model.ItemModelUtil;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
	@Inject(at = @At(value = "HEAD"), method = "addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/Sprite;)Ljava/util/List;", cancellable = true)
	private void onHeadAddLayerElements(int layer, String key, Sprite sprite, CallbackInfoReturnable<List<ModelElement>> cir) {
		if (ItemModelFix.getConfig().getOptions().generationType == ItemModelGenerationType.OUTLINE) {
			cir.setReturnValue(ItemModelUtil.createOutlineLayerElements(layer, key, sprite));
		} else if (ItemModelFix.getConfig().getOptions().generationType == ItemModelGenerationType.PIXEL) {
			cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, sprite));
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/Sprite;)Ljava/util/List;", locals = LocalCapture.CAPTURE_FAILHARD)
	private void onTailAddLayerElements(int layer, String key, Sprite sprite, CallbackInfoReturnable<List<ModelElement>> cir, Map<Direction, ModelElementFace> map, List<ModelElement> list) {
		if (ItemModelFix.getConfig().getOptions().generationType == ItemModelGenerationType.UNLERP) {
			ItemModelUtil.unlerpElements(list, sprite.getAnimationFrameDelta());
		}
	}
}
