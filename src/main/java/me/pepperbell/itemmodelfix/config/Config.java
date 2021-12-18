package me.pepperbell.itemmodelfix.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.pepperbell.itemmodelfix.ItemModelFix;
import me.pepperbell.itemmodelfix.model.ItemModelGenerationType;

public class Config {
	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	private File file;
	private Options options = null;

	public Config(File file) {
		this.file = file;
	}

	public Options getOptions() {
		return options;
	}

	public void load() {
		if (file.exists()) {
			try (FileReader reader = new FileReader(file)) {
				options = GSON.fromJson(reader, Options.class);
			} catch (IOException e) {
				ItemModelFix.LOGGER.error("Error loading config", e);
			}
			if (options != null) {
				if (options.replaceInvalidOptions(Options.DEFAULT)) {
					save();
				}
			}
		}
		if (options == null) {
			options = new Options();
			save();
		}
	}

	public void save() {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(GSON.toJson(options));
		} catch (IOException e) {
			ItemModelFix.LOGGER.error("Error saving config", e);
		}
	}

	public static class Options {
		public static final Options DEFAULT = new Options();

		public ItemModelGenerationType generationType = ItemModelGenerationType.OUTLINE;

		public boolean replaceInvalidOptions(Options options) {
			boolean invalid = false;
			if (generationType == null) {
				generationType = options.generationType;
				invalid = true;
			}
			return invalid;
		}
	}
}
