package com.github.gotochan;

import org.bukkit.configuration.file.FileConfiguration;

public class SkullConfigrable
{
	public SkullGetter plugin;
	public ConfigAccessor configAccessor;
	public FileConfiguration config;
	
	public boolean isEnableEconomy;
	public int EconomyValue;
	
	public SkullConfigrable(SkullGetter plugin)
	{
		this.plugin = plugin;
		loadConfig();
	}
	
	public void loadConfig()
	{
		configAccessor = new ConfigAccessor(plugin, "config.yml");
		configAccessor.saveDefaultConfig();
		FileConfiguration config = configAccessor.getConfig();
		
		isEnableEconomy = config.getBoolean("Economy");
		EconomyValue = config.getInt("EconomyValue");
	}
	
	public void save()
	{
		FileConfiguration conf = plugin.getConfig();
		conf.set("Economy", isEnableEconomy);
		conf.set("EconomyValue", EconomyValue);
		plugin.saveConfig();
	}
	
	public int getEconomyValue()
	{
		return this.EconomyValue;
	}
	
	public boolean isEnableEconomy()
	{
		return this.isEnableEconomy;
	}
}
