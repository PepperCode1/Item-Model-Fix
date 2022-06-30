package me.pepperbell.itemmodelfix.config;

import java.util.Locale;
import java.util.Optional;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;

import me.pepperbell.itemmodelfix.model.ItemModelGenerationType;
import me.pepperbell.itemmodelfix.util.ParsingUtil;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClothConfigFactory implements ConfigScreenFactory<Screen> {
	private Config config;

	public ClothConfigFactory(Config config) {
		this.config = config;
	}

	@Override
	public Screen create(Screen parent) {
		SavingRunnable savingRunnable = new SavingRunnable();

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(Text.translatable("screen.itemmodelfix.config.title"))
				.setSavingRunnable(savingRunnable);
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.itemmodelfix.general"));
		general.addEntry(entryBuilder.startEnumSelector(Text.translatable("option.itemmodelfix.generation_type"), ItemModelGenerationType.class, config.getOptions().generationType)
				.setSaveConsumer((value) -> {
					if (config.getOptions().generationType != value) {
						savingRunnable.reloadResources = true;
					}
					config.getOptions().generationType = value;
				})
				.setEnumNameProvider((value) -> {
					return Text.translatable("option.itemmodelfix.generation_type." + value.name().toLowerCase(Locale.ROOT));
				})
				.setTooltipSupplier((value) -> {
					return Optional.ofNullable(ParsingUtil.parseNewlines("option.itemmodelfix.generation_type." + value.name().toLowerCase(Locale.ROOT) + ".tooltip"));
				})
				.setDefaultValue(Config.Options.DEFAULT.generationType)
				.build());

		return builder.build();
	}

	private class SavingRunnable implements Runnable {
		public boolean reloadResources = false;

		@Override
		public void run() {
			config.save();
			if (reloadResources) {
				MinecraftClient.getInstance().reloadResources();
			}
			reloadResources = false;
		}
	}
}
