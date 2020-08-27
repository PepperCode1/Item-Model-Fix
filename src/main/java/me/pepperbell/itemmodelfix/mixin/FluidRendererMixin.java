package me.pepperbell.itemmodelfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.render.block.FluidRenderer;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
	@ModifyConstant(method="render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/fluid/FluidState;)Z", constant={@Constant(floatValue=0.001F)})
	public float onRender(float value) {
		return 0;
	}
	
	@ModifyConstant(method="render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/fluid/FluidState;)Z", constant={@Constant(doubleValue=0.0010000000474974513D)})
	public double onRender(double value) {
		return 0;
	}
}
