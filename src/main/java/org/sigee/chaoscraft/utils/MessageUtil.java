package org.sigee.chaoscraft.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

/**
 * Message Handler
 *
 * @author : sigee-min
 * @fileName : MessageUtil
 * @since : 2023/04/14
 */
public class MessageUtil {
    private static final String PREFIX = ChatColor.BLUE + "[ChaosCraft]: ";
    private static final String LWPREFIX = ChatColor.BLUE + "[CSC]: ";

    public static void printMsgToApiSender(Player sender, ScriptCode scriptCode){
        if(sender != null) {
            printMsgToPlayer(ChatMessageType.CHAT, sender, scriptCode);
            return;
        }
        printConsoleLog(scriptCode);
    }

    public static void printConsoleLog(ScriptCode scriptCode) {
        printConsoleLog(LanguageUtil.get(scriptCode));
    }

    public static void printConsoleLog(String msg){
        Bukkit.getConsoleSender().sendMessage(PREFIX + msg);
    }


    public static void printMsgToPlayers(ChatMessageType type, ScriptCode scriptCode){
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            printMsgToPlayer(type, player, scriptCode);
        });
    }

    public static void printMsgToPlayers(ChatMessageType type, String msg){
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            printMsgToPlayer(type, player, msg);
        });
    }

    public static void printMsgToPlayer(ChatMessageType type, Player player, ScriptCode scriptCode) {
        printMsgToPlayer(type, player, LanguageUtil.get(player, scriptCode));
    }

    public static void printMsgToPlayer(ChatMessageType type, Player player, String msg){
        if(ChatMessageType.ACTION_BAR.equals(type)){
            player.spigot().sendMessage(type,TextComponent.fromLegacyText(LWPREFIX + msg));
            return;
        }

        player.spigot().sendMessage(type,TextComponent.fromLegacyText(PREFIX + ChatColor.GREEN + msg));
    }

}
