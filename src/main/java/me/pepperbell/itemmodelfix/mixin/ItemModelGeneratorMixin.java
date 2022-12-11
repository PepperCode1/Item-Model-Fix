package me.pepperbell.itemmodelfix.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import me.pepperbell.itemmodelfix.ItemModelFix;
import me.pepperbell.itemmodelfix.model.ItemModelGenerationType;
import me.pepperbell.itemmodelfix.model.ItemModelUtil;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.util.math.Direction;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
	@Unique
	private Sprite itemmodelfix$sprite;

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/Sprite;getContents()Lnet/minecraft/client/texture/SpriteContents;"), method = "create")
	private SpriteContents captureSprite(Sprite sprite) {
		this.itemmodelfix$sprite = sprite;
		return sprite.getContents();
	}

	@Inject(at = @At(value = "RETURN"), method = "create")
	private void releaseSprite(CallbackInfoReturnable<JsonUnbakedModel> cir) {
		this.itemmodelfix$sprite = null;
	}

	@Inject(at = @At(value = "HEAD"), method = "addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/SpriteContents;)Ljava/util/List;", cancellable = true)
	private void onHeadAddLayerElements(int layer, String key, SpriteContents contents, CallbackInfoReturnable<List<ModelElement>> cir) {
		if (ItemModelFix.getConfig().getOptions().generationType == ItemModelGenerationType.OUTLINE) {
			cir.setReturnValue(ItemModelUtil.createOutlineLayerElements(layer, key, this.itemmodelfix$sprite));
		} else if (ItemModelFix.getConfig().getOptions().generationType == ItemModelGenerationType.PIXEL) {
			cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, contents));
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/SpriteContents;)Ljava/util/List;", locals = LocalCapture.CAPTURE_FAILHARD)
	private void onTailAddLayerElements(int layer, String key, SpriteContents contents, CallbackInfoReturnable<List<ModelElement>> cir, Map<Direction, ModelElementFace> map, List<ModelElement> list) {
		if (ItemModelFix.getConfig().getOptions().generationType == ItemModelGenerationType.UNLERP) {
			ItemModelUtil.unlerpElements(list, this.itemmodelfix$sprite.getAnimationFrameDelta());
		}
	}
}
