package me.pepperbell.itemmodelfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import me.pepperbell.itemmodelfix.ItemModelFix;
import net.minecraft.client.render.block.FluidRenderer;

@Mixin(value=FluidRenderer.class, priority=100)
public class FluidRendererMixin {
	@ModifyConstant(method="render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/fluid/FluidState;)Z", constant={@Constant(floatValue=0.001F)})
	public float onRender(float value) {
		return ItemModelFix.getConfig().getOptions().fixFluidRendering ? 0 : value;
	}
	
	@ModifyConstant(method="render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/fluid/FluidState;)Z", constant={@Constant(doubleValue=0.0010000000474974513D)})
	public double onRender(double value) {
		return ItemModelFix.getConfig().getOptions().fixFluidRendering ? 0 : value;
	}
}
