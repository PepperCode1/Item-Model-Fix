package me.pepperbell.itemmodelfix.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.pepperbell.itemmodelfix.util.ModelGenerationType;

public class Config {
	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	
	private File file;
	private Options options;
	
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
				e.printStackTrace();
			}
		} else {
			options = new Options();
			save();
		}
	}
	
	public void save() {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(GSON.toJson(options));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class Options {
		public ModelGenerationType generationType = ModelGenerationType.UNLERP;
		public boolean fixFluidRendering = false;
	}
}
