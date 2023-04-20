package org.sigee.chaoscraft.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.sigee.chaoscraft.ChaosCraft;
import org.sigee.chaoscraft.utils.enums.ConfigOption;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Managing Configuration Files
 *
 * @author : sigee-min
 * @fileName : ConfigFileUtil
 * @since : 2023/04/14
 */
public class ConfigFileUtil {
    private ConfigFileUtil() {}
    public static FileConfiguration config;
    private static String PLUGIN_DATA_PATH;


    /**
     * Load default configuration.
     */
    public static void loadDefaultConfiguration() {
        config = ChaosCraft.getInstance().getConfig();
        config.options().copyDefaults(true);
        ChaosCraft.getInstance().saveConfig();

        PLUGIN_DATA_PATH = ChaosCraft.getInstance().getDataFolder().getPath() + "/";
    }

    /**
     * Load configuraion.
     *
     * @param path the path
     */
    public static void loadInternalConfiguraion(String path) {
        Boolean isReplace = (Boolean) getDefaultConfigOption(ConfigOption.REPLACE_CONFIG);
        ChaosCraft.getInstance().saveResource(path, isReplace);
    }

    /**
     * Gets resource as string.
     *
     * @param path the path
     * @return the resource as string
     */
    private static String getResourceAsString(String path) {
        InputStream inputStream = ChaosCraft.getInstance().getResource(path);
        if (inputStream == null)
            return null;
        try {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Gets string as file configuration.
     *
     * @param path the path
     * @return the string as file configuration
     */
    public static FileConfiguration getStringAsFileConfiguration(String path) {
        var data = getResourceAsString(path);
        if(data == null)
            return null;
        return YamlConfiguration.loadConfiguration(new StringReader(data));
    }

    /**
     * Gets config option.
     *
     * @param opt the option code
     * @return the config option
     */
    public static Object getDefaultConfigOption(ConfigOption opt) {
        return config.get(opt.name().toLowerCase().replace("_","-"));
    }

    public static FileConfiguration getExtFileConfiguration(String path) {
        File file = new File(PLUGIN_DATA_PATH + path);
        return YamlConfiguration.loadConfiguration(file);
    }
    public static void changeProperty(String path, String key, String value) {
        var conf = getExtFileConfiguration(path);
        var file = new File(PLUGIN_DATA_PATH + path);
        conf.set(key, value);
        try {
            conf.save(file);
            MessageUtil.printConsoleLog("CHANGE_PROPERTY : " + key + " " + value.toString() + " " + path);
        }
        catch (Exception e){
            MessageUtil.printConsoleLog("ERROR_CHANGE_PROPERTY : " + ChaosCraft.getInstance().getDataFolder().getPath() + path);
        }

    }
}
