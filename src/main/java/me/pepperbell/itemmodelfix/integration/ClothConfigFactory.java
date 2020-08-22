package me.pepperbell.itemmodelfix.integration;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import me.pepperbell.itemmodelfix.ItemModelFix;
import me.pepperbell.itemmodelfix.util.ModelGenerationType;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class ClothConfigFactory implements ConfigScreenFactory<Screen> {
	private boolean reloadResources = false;
	private boolean reloadWorldRenderer = false;
	
	@Override
	public Screen create(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(new LiteralText("Item Model Fix Configuration"))
				.setSavingRunnable(() -> {
					ItemModelFix.getConfig().save();
					if (reloadResources) {
						MinecraftClient.getInstance().reloadResources();
					}
					if (reloadWorldRenderer) {
						MinecraftClient.getInstance().worldRenderer.reload();
					}
					reloadResources = false;
					reloadWorldRenderer = false;
				});
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		
		ConfigCategory general = builder.getOrCreateCategory(new LiteralText("General"));
		general.addEntry(entryBuilder.startEnumSelector(new LiteralText("Item Model Generation Type"), ModelGenerationType.class, ItemModelFix.getConfig().getOptions().generationType)
				.setSaveConsumer((value) -> {
					if (ItemModelFix.getConfig().getOptions().generationType != value) {
						reloadResources = true;
					}
					ItemModelFix.getConfig().getOptions().generationType = value;
				})
				.setTooltipSupplier((value) -> {
					return value.getTooltip();
				})
				.build()
				);
		
		ConfigCategory experimental = builder.getOrCreateCategory(new LiteralText("Experimental"));
		experimental.addEntry(entryBuilder.startBooleanToggle(new LiteralText("Fix Fluid Rendering"), ItemModelFix.getConfig().getOptions().fixFluidRendering)
				.setSaveConsumer((value) -> {
					if (ItemModelFix.getConfig().getOptions().fixFluidRendering != value) {
						reloadWorldRenderer = true;
					}
					ItemModelFix.getConfig().getOptions().fixFluidRendering = value;
				})
				.setTooltip(new LiteralText("Performance impact: \u00A7b\u00A7lnone"), new LiteralText("\u00A7cIncompatible with Sodium."), new LiteralText("Fixes edges between fluid texture sides."))
				.build());
		
		return builder.build();
	}
}
