package org.sigee.chaoscraft.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.ChaosCraft;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.utils.enums.ConfigOption;
import org.sigee.chaoscraft.utils.enums.LanguageCode;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

/**
 * Managing Console and Player Language
 *
 * @author : sigee-min
 * @fileName : LanguageUtil
 * @since : 2023/04/14
 */
public class LanguageUtil {
    private LanguageUtil(){}

    private static final String USER_LANG_FILE_PATH = "lang/user-lang.yml";
    private static final HashMap<LanguageCode, FileConfiguration> dict = initDict();
    private static final LanguageCode CONSOLE_LANGUAGE_CODE = LanguageCode.valueOf(ConfigFileUtil.getDefaultConfigOption(ConfigOption.CONSOLE_LANGUAGE).toString().toUpperCase(Locale.ROOT));
    private static final LanguageCode DEFAULT_LANGUAGE_CODE = LanguageCode.valueOf(ConfigFileUtil.getDefaultConfigOption(ConfigOption.DEFAULT_LANGUAGE).toString().toUpperCase(Locale.ROOT));

    /**
     * Load, Init Language.
     */
    public static void loadLanguageConfigiguration() {
        ConfigFileUtil.loadInternalConfiguraion(USER_LANG_FILE_PATH);
        Arrays.stream(LanguageCode.values()).forEach(lang -> {
            var path = "lang/" + lang.toString().toLowerCase() + ".yml";
            ConfigFileUtil.loadInternalConfiguraion(path);
        });
    }
    private static HashMap<LanguageCode, FileConfiguration> initDict() {
        var dict = new HashMap<LanguageCode, FileConfiguration> ();
        Arrays.stream(LanguageCode.values()).forEach(lang -> {
            var config = ConfigFileUtil.getStringAsFileConfiguration("lang/" + lang.toString().toLowerCase() + ".yml");
            dict.put(lang, config);
        });
        return dict;
    }

    private static FileConfiguration getConfig(LanguageCode lang) {
        return dict.get(lang);
    }

    private static String getNullScript(LanguageCode lang, ScriptCode script) {
        return "[Lang: " + lang.name() + " ], [Script: "  + script.name() + " ] IS NOT FOUNDED...";
    }

    private static String loadScript(LanguageCode lang, ScriptCode script) {
        var config = getConfig(lang);
        try {
            var data = config.getString(script.name());
            if(data == null){
                return getNullScript(lang, script);
            }
            return data;
        }
        catch (Exception e) {
            MessageUtil.printConsoleLog("[Lang: " + lang.name() + "], [Script: "  + script.name() + "] IS NOT FOUNDED...");
            MessageUtil.printConsoleLog(e.getMessage());
        }
        return getNullScript(lang, script);
    }

    /**
     * Get Console Script with Console Language. Check Configuraion_Option.
     *
     * @param script the script
     * @return Specific Language String
     */
    public static String get(ScriptCode script) {
        return loadScript(CONSOLE_LANGUAGE_CODE, script);
    }

    /**
     * Get Player Script with Player own Language.
     *
     * @param player the player
     * @param script the script code
     * @return Specific Language String
     */
    public static String get(Player player, ScriptCode script) {
        LanguageCode lang = CONSOLE_LANGUAGE_CODE;
        if(player != null)
            lang = ChaosPlayer.getChaosPlayer(player).getPlayerLanguage();
        return get(lang, script);
    }

    /**
     * Get Script with Language.
     *
     * @param lang   the language code
     * @param script the script code
     * @return Specific Language String
     */
    public static String get(LanguageCode lang, ScriptCode script) {
        if(lang == null)
            lang = DEFAULT_LANGUAGE_CODE;

        return loadScript(lang, script);
    }

    public static LanguageCode loadPlayerLanguage(Player player) {
        var conf = ConfigFileUtil.getExtFileConfiguration(USER_LANG_FILE_PATH);
        var uuid = player.getUniqueId().toString();
        if(!conf.contains(uuid)) {
            setPlayerLanguage(player, DEFAULT_LANGUAGE_CODE);
            return DEFAULT_LANGUAGE_CODE;
        }
        return LanguageCode.valueOf(conf.getString(uuid));
    }

    public static void setPlayerLanguage(Player player, LanguageCode lang) {
        var uuid = player.getUniqueId().toString();
        ConfigFileUtil.changeProperty(USER_LANG_FILE_PATH, uuid, lang.name());
    }

}
