package me.pepperbell.itemmodelfix.integration;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import me.pepperbell.itemmodelfix.ItemModelFix;
import net.fabricmc.loader.api.FabricLoader;

public class MixinConfigPluginImpl implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		boolean fixFluidRendering = ItemModelFix.getConfig().getOptions().fixFluidRendering;
		boolean sodiumLoaded = FabricLoader.getInstance().isModLoaded("sodium");
		boolean frexLoaded = FabricLoader.getInstance().isModLoaded("frex");
		if (targetClassName.equals("net.minecraft.client.render.block.FluidRenderer")) {
			return fixFluidRendering;
		}
		if (targetClassName.equals("me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer")) {
			return fixFluidRendering && sodiumLoaded;
		}
		if (targetClassName.equals("grondag.frex.api.fluid.AbstractFluidModel")) {
			return fixFluidRendering && frexLoaded;
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
