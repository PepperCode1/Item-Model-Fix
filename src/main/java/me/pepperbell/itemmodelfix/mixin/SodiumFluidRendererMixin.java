package me.pepperbell.itemmodelfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;

@Mixin(value=FluidRenderer.class, remap=false)
public class SodiumFluidRendererMixin {
	//@ModifyConstant(method="render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/util/math/BlockPos;Lme/jellysquid/mods/sodium/client/model/quad/sink/ModelQuadSinkDelegate;)Z", constant={@Constant(floatValue=0.001F)})
	@ModifyConstant(method="render(Lnet/minecraft/class_1920;Lnet/minecraft/class_3610;Lnet/minecraft/class_2338;Lme/jellysquid/mods/sodium/client/model/quad/sink/ModelQuadSinkDelegate;)Z", constant={@Constant(floatValue=0.001F)})
	public float onRender0(float value) {
		return 0;
	}
	
	//@ModifyConstant(method="render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/util/math/BlockPos;Lme/jellysquid/mods/sodium/client/model/quad/sink/ModelQuadSinkDelegate;)Z", constant={@Constant(floatValue=0.999F)})
	@ModifyConstant(method="render(Lnet/minecraft/class_1920;Lnet/minecraft/class_3610;Lnet/minecraft/class_2338;Lme/jellysquid/mods/sodium/client/model/quad/sink/ModelQuadSinkDelegate;)Z", constant={@Constant(floatValue=0.999F)})
	public float onRender1(float value) {
		return 1;
	}
}
