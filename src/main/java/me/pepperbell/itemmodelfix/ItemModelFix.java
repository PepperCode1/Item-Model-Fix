package me.pepperbell.itemmodelfix;

import java.io.File;
import java.nio.file.Path;

import me.pepperbell.itemmodelfix.data.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class ItemModelFix implements ClientModInitializer {
	private static Config config;
	
	public static Config getConfig() {
		return config;
	}
	
	@Override
	public void onInitializeClient() {
		Path configPath = FabricLoader.getInstance().getConfigDir();
		File configFile = new File(configPath.toFile(), "itemmodelfix.json");
		config = new Config(configFile);
		config.load();
	}
}
