package org.sigee.chaoscraft.events;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.sigee.chaoscraft.api.Game;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.api.entities.enums.GameState;
import org.sigee.chaoscraft.api.entities.enums.PlayerType;
import org.sigee.chaoscraft.utils.LanguageUtil;
import org.sigee.chaoscraft.utils.MessageUtil;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : JoinQuitHandler
 * @since : 2023/04/14
 */
public class JoinQuitHandler {

    public void joinPlayer(PlayerJoinEvent event) {
        var cplayer = new ChaosPlayer(event.getPlayer());
        cplayer.getPlayer().setGameMode(GameMode.ADVENTURE);
        event.setJoinMessage(null);
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, event.getPlayer(), ScriptCode.PLAYER_JOIN_MSG);

        var curState = Game.getInstance().getGameState();
        if(GameState.FROZEN.equals(curState) || GameState.STARTED.equals(curState)) {
            MessageUtil.printMsgToPlayer(
                    ChatMessageType.CHAT,
                    cplayer.getPlayer(),
                    LanguageUtil.get(cplayer.getPlayer(), ScriptCode.GAME_ALREADY_STARTED));
            cplayer.updateType(PlayerType.SPECTATOR);
            cplayer.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }

    public void quitPlayer(PlayerQuitEvent event) {
        var cplayer = ChaosPlayer.getChaosPlayer(event.getPlayer());
        var curState = Game.getInstance().getGameState();
        if(GameState.FROZEN.equals(curState) || GameState.STARTED.equals(curState)) {

            // TODO 플레이어 탈락 이벤트
        }

        ChaosPlayer.deleteChaosPlayer(cplayer.getPlayer());
    }


}
