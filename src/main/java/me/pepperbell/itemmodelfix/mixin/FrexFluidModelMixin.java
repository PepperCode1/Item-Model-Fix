package me.pepperbell.itemmodelfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import grondag.frex.api.fluid.AbstractFluidModel;

@Mixin(value=AbstractFluidModel.class, remap=false)
public class FrexFluidModelMixin {
	@ModifyConstant(method="emitBlockQuads(Lnet/minecraft/class_1920;Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Ljava/util/function/Supplier;Lnet/fabricmc/fabric/api/renderer/v1/render/RenderContext;)V", constant={@Constant(floatValue=0.001F)})
	public float onRender0(float value) {
		return 0;
	}
	
	@ModifyConstant(method="emitBlockQuads(Lnet/minecraft/class_1920;Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Ljava/util/function/Supplier;Lnet/fabricmc/fabric/api/renderer/v1/render/RenderContext;)V", constant={@Constant(floatValue=0.999F)})
	public float onRender1(float value) {
		return 1;
	}
}
