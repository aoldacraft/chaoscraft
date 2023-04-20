package org.sigee.chaoscraft.commands;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.api.core.view.ViewHandler;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.api.entities.enums.PlayerType;
import org.sigee.chaoscraft.utils.LanguageUtil;
import org.sigee.chaoscraft.utils.MessageUtil;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : PlayerEtcCMD
 * @since : 2023/04/15
 */
public class PlayerEtcCMD {

    public void showPlayerStatus(Player player) {
        var status = "\n" +
                LanguageUtil.get(player, ScriptCode.NAME) + ": " + player.getName() + "\n" +
                LanguageUtil.get(player, ScriptCode.LANGUAGE) + ": " + ChaosPlayer.getChaosPlayer(player).getPlayerLanguage() + "\n" +
                LanguageUtil.get(player, ScriptCode.PLAYER_TYPE) + ": " + ChaosPlayer.getChaosPlayer(player).getType().name() + "\n";
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, status);
    }

    public void showPlayerHelp(Player player) {
        var userCMD = "\n" +
                "/csc lang\n" +
                "/csc game\n" +
                "/csc world\n" +
                "----------";
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, userCMD);
    }

    public void joinPlayerGame(Player player) {
        ChaosPlayer.getChaosPlayer(player)
                .updateType(PlayerType.PARTICIPANT);
    }

    public void specPlayerGame(Player player) {
        ChaosPlayer.getChaosPlayer(player)
                .updateType(PlayerType.SPECTATOR);
    }

    public void exitPlayerGame(Player player) {
        ChaosPlayer.getChaosPlayer(player)
                .updateType(PlayerType.INACTIVE);
    }
}
