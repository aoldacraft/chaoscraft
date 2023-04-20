package org.sigee.chaoscraft.commands;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.api.Game;
import org.sigee.chaoscraft.commands.interfaces.CMD;
import org.sigee.chaoscraft.utils.MessageUtil;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : PlayerGameCMD
 * @since : 2023/04/15
 */
public class PlayerGameCMD implements CMD {
    @Override
    public void routeCommands(Player sender, String[] args) {
        if (args.length < 2) {
            showHelp(sender);
            return;
        }
        if(args[1].equalsIgnoreCase("help")) {
            showHelp(sender);
            return;
        }
        if(args[1].equalsIgnoreCase("start")) {
            execGameStart(sender);
            return;
        }
        if(args[1].equalsIgnoreCase("stop")) {
            execGameStop(sender);
        }
    }

    private void showHelp(Player player) {
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player,
                "\n" +
                        "/csc game help\n" +
                        "/csc game start\n" +
                        "/csc game stop\n" +
                        "/csc game info\n" +
                        "----------");
    }

    private void execGameStart(Player player) {
        Game.getInstance().updateGameState(player);
    }

    private void execGameStop(Player player) {
        Game.getInstance().stopGame();
    }




}
