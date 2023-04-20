package org.sigee.chaoscraft.api.entities;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.api.entities.enums.PlayerType;
import org.sigee.chaoscraft.utils.LanguageUtil;
import org.sigee.chaoscraft.utils.MessageUtil;
import org.sigee.chaoscraft.utils.enums.LanguageCode;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Participants Management
 *
 * @author : sigee-min
 * @fileName : ChaosPlayer
 * @since : 2023/04/14
 */
public class ChaosPlayer {
    private static final Map<Player, ChaosPlayer> onlinePlayers = new HashMap<>();
    public static void deleteChaosPlayer(Player p) {
        onlinePlayers.remove(p);
    }
    public static ChaosPlayer getChaosPlayer(Player p){
        return onlinePlayers.get(p);
    }

    public static Set<ChaosPlayer> getChaosPlayers() {
        return onlinePlayers.keySet().stream()
                .map(ChaosPlayer::getChaosPlayer)
                .collect(Collectors.toSet());
    }

    public static Set<ChaosPlayer> getChaosPlayerSetByType(PlayerType type) {
        return onlinePlayers.keySet().stream()
                .map(ChaosPlayer::getChaosPlayer)
                .filter(chaosPlayer -> chaosPlayer.getType().equals(type))
                .collect(Collectors.toSet());
    }

    public ChaosPlayer(Player p) {
        onlinePlayers.remove(p);
        onlinePlayers.put(p, this); // 무조건 제일 위

        this.player = p;
        this.languageCode = LanguageUtil.loadPlayerLanguage(p);
        this.stat = new ChaosStat();
        getChaosStats().getPlayerStats().keySet()
                .forEach(attribute -> updateChaosStat(attribute, 0));
    }

    private final Player player;
    public Player getPlayer() {
        return player;
    }

    private PlayerType type = PlayerType.INACTIVE;
    public PlayerType getType() {
        return type;
    }
    public void updateType(PlayerType type) {
        if(PlayerType.PARTICIPANT.equals(type)) {
            updateTypeToParticipant(player);
        }
        else if (PlayerType.SPECTATOR.equals(type)) {
            updateTypeToSPECTATOR(player);
        }
        else if (PlayerType.INACTIVE.equals(type)) {
            updateTypeToInActive(player);
        }
        this.type = type;
    }

    private void updateTypeToParticipant(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, ScriptCode.PLAYER_JOIN_PARTICIPANT);
    }
    private void updateTypeToSPECTATOR(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, ScriptCode.PLAYER_JOIN_SPECTATOR);
    }
    private void updateTypeToInActive(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, ScriptCode.PLAYER_JOIN_INACTIVE);
    }

    private LanguageCode languageCode;
    public LanguageCode getPlayerLanguage() {
        return languageCode;
    }
    public void changePlayerLanguage(LanguageCode lang) {
        LanguageUtil.setPlayerLanguage(this.player, lang);
        this.languageCode = LanguageUtil.loadPlayerLanguage(this.player);
    }

    private ChaosStat stat;
    public ChaosStat getChaosStats() {
        return stat;
    }
    public Integer getChaosStat(Attribute attribute) {
        return getChaosStats().getPlayerStat(attribute);
    }
    public void updateChaosStat(Attribute attribute, Integer val) {
        getChaosStats().updateStat(getPlayer(), attribute, val);
    }

}
