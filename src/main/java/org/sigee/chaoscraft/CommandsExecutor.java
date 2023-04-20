package org.sigee.chaoscraft;


import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.commands.PlayerEtcCMD;
import org.sigee.chaoscraft.commands.PlayerGameCMD;
import org.sigee.chaoscraft.commands.PlayerLangCMD;
import org.sigee.chaoscraft.commands.interfaces.CMD;
import org.sigee.chaoscraft.utils.MessageUtil;
import org.sigee.chaoscraft.api.core.SetGameZone;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

public class CommandsExecutor implements CommandExecutor {

    private final CMD playerLangCMD = new PlayerLangCMD();
    private final CMD playerGameCMD = new PlayerGameCMD();
    private final PlayerEtcCMD playerEtcCMD = new PlayerEtcCMD();


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            if(args.length < 1) {
                MessageUtil.printConsoleLog(ScriptCode.NOT_CONSOLE_CMD);
                MessageUtil.printConsoleLog("ex: scs console help");
                return true;
            } else if ("console".equalsIgnoreCase(args[0])) {
                return true;
            }
            return true;
        }
        if(args.length < 1) {
            MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, "csc help");
            return false;
        }
        if(args[0].equalsIgnoreCase("help")) {
            playerEtcCMD.showPlayerHelp(player);
            return false;
        }
        if(args[0].equalsIgnoreCase("lang")) {
            playerLangCMD.routeCommands(player, args);
            return false;
        }
        if(args[0].equalsIgnoreCase("status")) {
            playerEtcCMD.showPlayerStatus(player);
            return false;
        }
        if(args[0].equalsIgnoreCase("join")) {
            if(args.length > 1 && args[1].equalsIgnoreCase("spec")) {
                playerEtcCMD.specPlayerGame(player);
                return false;
            }
            playerEtcCMD.joinPlayerGame(player);
            return false;
        }
        if(args[0].equalsIgnoreCase("quit")) {
            playerEtcCMD.exitPlayerGame(player);
            return false;
        }

        if(!player.isOp()) {
            return false;
        }

        if(args[0].equalsIgnoreCase("game")) {
            playerGameCMD.routeCommands(player, args);
        }

        if(args[0].equalsIgnoreCase("world")) {
            SetGameZone.getInstance().onCommands(player, args);
        }

        return false;
    }
}
