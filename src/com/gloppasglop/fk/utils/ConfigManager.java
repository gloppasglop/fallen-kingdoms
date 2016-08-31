package com.gloppasglop.fk.utils;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by christopheroux on 31/08/16.
 */
public class ConfigManager {

    private Plugin plugin;
    private FileConfiguration config;
    private FileConfiguration data;
    private File configfile;
    private File datafile;


    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }


    public void setup() {

        configfile = new File(plugin.getDataFolder(),"config.yml");
        config = plugin.getConfig();

        config.options().copyDefaults(true);
        saveConfig();

        if (!plugin.getDataFolder().exists()) {

            try {
                plugin.getDataFolder().createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("could not create config folder");
            }

        }

        datafile = new File(plugin.getDataFolder(),"data.yml");

        if (!datafile.exists()) {

            try {
                datafile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("could not create data.yml file");
            }
        }

        data = YamlConfiguration.loadConfiguration(datafile);


    }

    public FileConfiguration getData() {
        return data;
    }

    public void saveData() {
        try {
            data.save(datafile);
        } catch (IOException e) {
            plugin.getLogger().severe("could not create save data.yml");
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(datafile);
    }

    public FileConfiguration getConfig() {
        return config;
    }


    public void saveConfig() {
        try {
            config.save(configfile);
        } catch (IOException e) {
            plugin.getLogger().severe("could not create save config.yml");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configfile);
    }
}
