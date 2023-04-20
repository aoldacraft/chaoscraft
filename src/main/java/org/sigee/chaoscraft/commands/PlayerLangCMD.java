package org.sigee.chaoscraft.commands;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.commands.interfaces.CMD;
import org.sigee.chaoscraft.utils.MessageUtil;
import org.sigee.chaoscraft.utils.enums.LanguageCode;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

import java.util.Arrays;

public class PlayerLangCMD implements CMD {

    public void routeCommands(Player player, String[] args) {
        if(args.length < 2) {
            helpLanguage(player);
            return;
        }
        if(args[1].equalsIgnoreCase("help")){
            helpLanguage(player);
            return;
        }
        if(args[1].equalsIgnoreCase("change")){
            if(args[2] == null) {
                helpLanguage(player);
                return;
            }
            try {
                changeLanguage(player, args[2]);
            }
            catch (Exception ignored) {}
            return;
        }
        if(args[1].equalsIgnoreCase("showlist")){
            showLanguageList(player);
            return;
        }
        if(args[1].equalsIgnoreCase("showMine")){
            getPlayerOwnLanguage(player);
            return;
        }
        helpLanguage(player);
    }

    private void helpLanguage(Player player) {
        var str = "\n" +
                "/csc lang help \n" +
                "/csc lang change {code} \n" +
                "/csc lang showList \n" +
                "/csc lang showMine \n" +
                "----------";
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, str);
    }

    private void changeLanguage(Player player, String langCode) {
        ChaosPlayer.getChaosPlayer(player)
                .changePlayerLanguage(LanguageCode.valueOf(langCode.toUpperCase()));
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, ScriptCode.LANG_CHANGE);
    }

    private void showLanguageList(Player player) {
        MessageUtil.printMsgToPlayer(
                ChatMessageType.CHAT,
                player,
                Arrays.toString(LanguageCode.values()));
    }

    private void getPlayerOwnLanguage(Player player) {
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, "Language: " + ChaosPlayer.getChaosPlayer(player).getPlayerLanguage().name());
    }
}
