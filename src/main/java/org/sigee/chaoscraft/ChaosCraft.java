package org.sigee.chaoscraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.sigee.chaoscraft.api.core.view.ViewHandler;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.utils.ConfigFileUtil;
import org.sigee.chaoscraft.utils.LanguageUtil;
import org.sigee.chaoscraft.utils.MessageUtil;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

import java.util.Objects;

public final class ChaosCraft extends JavaPlugin {
    private static volatile ChaosCraft instance;

    public static synchronized ChaosCraft getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this; // 무조건 제일 위로...
        initPlugin();
        MessageUtil.printConsoleLog(ScriptCode.ON_ENABLE);
        ViewHandler.getInstance().stopScoreboardData();
    }

    @Override
    public void onDisable() {
        MessageUtil.printConsoleLog(ScriptCode.ON_DISABLE);
    }

    private void initPlugin() {
        ConfigFileUtil.loadDefaultConfiguration();
        LanguageUtil.loadLanguageConfigiguration();
        Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("csc")).setExecutor(new CommandsExecutor());

        Bukkit.getOnlinePlayers().forEach(ChaosPlayer::new);
    }
}
