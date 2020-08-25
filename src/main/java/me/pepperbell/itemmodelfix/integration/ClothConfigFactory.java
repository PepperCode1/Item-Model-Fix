package me.pepperbell.itemmodelfix.integration;

import java.util.Optional;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import me.pepperbell.itemmodelfix.ItemModelFix;
import me.pepperbell.itemmodelfix.util.ModelGenerationType;
import me.pepperbell.itemmodelfix.util.ParsingUtil;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class ClothConfigFactory implements ConfigScreenFactory<Screen> {
	private boolean reloadResources = false;
	
	@Override
	public Screen create(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(new TranslatableText("itemmodelfix.configuration.title"))
				.setSavingRunnable(() -> {
					ItemModelFix.getConfig().save();
					if (reloadResources) {
						MinecraftClient.getInstance().reloadResources();
					}
					reloadResources = false;
				});
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		
		ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("itemmodelfix.category.general"));
		general.addEntry(entryBuilder.startEnumSelector(new TranslatableText("itemmodelfix.options.generation_type"), ModelGenerationType.class, ItemModelFix.getConfig().getOptions().generationType)
				.setSaveConsumer((value) -> {
					if (ItemModelFix.getConfig().getOptions().generationType != value) {
						reloadResources = true;
					}
					ItemModelFix.getConfig().getOptions().generationType = value;
				})
				.setEnumNameProvider((value) -> {
					return new TranslatableText("itemmodelfix.options.generation_type."+value.toString().toLowerCase());
				})
				.setTooltipSupplier((value) -> {
					return Optional.ofNullable(ParsingUtil.parseNewlines("itemmodelfix.options.generation_type."+value.toString().toLowerCase()+".tooltip"));
				})
				.setDefaultValue(ModelGenerationType.UNLERP)
				.build());
		
		ConfigCategory experimental = builder.getOrCreateCategory(new TranslatableText("itemmodelfix.category.experimental"));
		experimental.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("itemmodelfix.options.fix_fluid_rendering"), ItemModelFix.getConfig().getOptions().fixFluidRendering)
				.setSaveConsumer((value) -> {
					ItemModelFix.getConfig().getOptions().fixFluidRendering = value;
				})
				.setTooltip(Optional.ofNullable(ParsingUtil.parseNewlines("itemmodelfix.options.fix_fluid_rendering.tooltip")))
				.setDefaultValue(false)
				.requireRestart()
				.build());
		
		return builder.build();
	}
}
