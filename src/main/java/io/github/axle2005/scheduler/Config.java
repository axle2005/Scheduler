package io.github.axle2005.scheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
	private Plugin plugin;
	private String filename;
	private File folder;
	private FileConfiguration config;
	private File configFile;

	public Config(Scheduler plugin, String filename) {
		this.plugin = plugin;

		if (!filename.endsWith(".yml")) {
			filename += ".yml";
		}
		this.filename = filename;
		this.plugin = plugin;
		this.folder = plugin.getDataFolder();
		this.config = null;
		this.configFile = null;

		reload();

	}

	@SuppressWarnings("null")
	private void firstRun(FileConfiguration config) throws Exception {

		
		
		config = this.config;
		if (getFileName().equalsIgnoreCase("config.yml")) {
			if (!ifPathExists("Name")) {
				set("Name", "Scheduler");
				set("ServerType","Spigot");
				List<String> configList = new ArrayList<String>();
				for(int i = 5; i <=60; i=i+5)
				{
					List<String> defaults = Arrays.asList("Replace Text");
					//String node = "ScheduledTasks"+i;
					//configList.add(i+"");
					this.getConfig().set("ScheduledTasks", defaults);
					
					
				}
				//this.getConfig().set("Interval."+configList, defaults);
			}
		} 
	
		save();
	}

	private String getFileName() {
		return filename;
	}

	public FileConfiguration getConfig() {
		if (config == null) {
			reload();
		}
		return config;
	}

	public void reload() {
		if (!this.folder.exists()) {
			try {
				if (this.folder.mkdir()) {
					this.plugin.getLogger().log(Level.INFO, "Folder " + this.folder.getName() + " created.");
				} else {
					this.plugin.getLogger().log(Level.WARNING,
							"Unable to create folder " + this.folder.getName() + ".");
				}
			} catch (Exception e) {

			}
		}
		configFile = new File(this.folder, this.filename);
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {

			}
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		try {
			firstRun(config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveDefaultConfig() {
		if (configFile == null) {
			configFile = new File(plugin.getDataFolder(), this.filename);
		}

		if (!configFile.exists()) {
			plugin.saveResource(this.filename, false);
		}
	}

	public void save() {
		if (config == null || configFile == null) {
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.WARNING, "Could not save config to " + configFile.getName(), ex);
		}
	}

	public void set(String path, Object o) {
		getConfig().set(path, o);
	}

	public void removeNode(String path) {
		getConfig().set(path, null);
	}

	public int getIntValue(String path) {
		return getConfig().getInt(path);
	}

	public List getStringList(String path) {
		return getConfig().getList(path);
	}
	
	public String getStringValue(String path) {
		return getConfig().getString(path);
	}

	public Boolean getBooleanValue(String path) {
		return getConfig().getBoolean(path);
	}

	public boolean ifPathExists(String path) {
		return getConfig().get(path) != null;
	}
}
