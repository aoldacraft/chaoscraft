package org.sigee.chaoscraft.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.api.core.ValidateGame;
import org.sigee.chaoscraft.api.core.view.ViewHandler;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.api.entities.enums.GameState;
import org.sigee.chaoscraft.api.entities.enums.PlayerType;
import org.sigee.chaoscraft.api.modules.mission.ChaosMission;
import org.sigee.chaoscraft.utils.ConfigFileUtil;
import org.sigee.chaoscraft.utils.GenRequirementsUtil;
import org.sigee.chaoscraft.api.core.SetGameZone;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : Game
 * @since : 2023/04/14
 */
public class Game {
    private Game() {}
    private final static Integer GameCode = 0;
    private final ValidateGame validateGame = new ValidateGame();
    private static final class InstanceHolder {
        private static final Game instance = new Game();
    }
    public static Game getInstance() {
        return InstanceHolder.instance;
    }

    private GameState gameState = GameState.NOT_STARTED;
    public GameState getGameState() {
        return gameState;
    }
    private void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public void updateGameState(Player sender) {
        if (GameState.NOT_STARTED.equals(gameState)) {
            SetGameZone.getInstance().clearSettingData();
            GenRequirementsUtil.scanGameWorldPos();
            setGameState(GameState.LOADING);
        }
        else if(GameState.LOADING.equals(gameState) && validateGame.updateLoading(sender)) {
            execGameGenRequirements();
            startModule();
            setGameState(GameState.STARTED);
        }
        else if(GameState.STARTED.equals(gameState)) {
            setGameState(GameState.FROZEN);
        }
        else if(GameState.FROZEN.equals(gameState)) {
            setGameState(GameState.STARTED);
        }
    }

    private void startModule() {
        if(ConfigFileUtil.config.getBoolean("mission")) {
            ChaosMission.getInstance()
                    .createMissions();
        }
    }

    public void stopGame() {
        setGameState(GameState.NOT_STARTED);
        ChaosPlayer.getChaosPlayerSetByType(PlayerType.PARTICIPANT)
                        .forEach(chaosPlayer -> chaosPlayer.updateType(PlayerType.INACTIVE));
        GenRequirementsUtil.resetWorldBlock();
        GenRequirementsUtil.resetChestBlocks();
        GenRequirementsUtil.clearChangedBlocks();
        GenRequirementsUtil.clearChangedItems();
        ViewHandler.getInstance().stopScoreboardData();
    }

    private void execGameGenRequirements() {
        GenRequirementsUtil.resetChestBlocks();
        GenRequirementsUtil.resetWorldBlock();

        GenRequirementsUtil.addChangedBlock(GameCode,Material.LECTERN);
        GenRequirementsUtil.addChangedItems(GameCode, returnDefaultInBoxItems());

        GenRequirementsUtil.makeNecessaryBlock(GameCode);
        GenRequirementsUtil.insertItemToChest(GameCode);
    }

    private ArrayList<AbstractMap.SimpleEntry<Material, Integer>> returnDefaultInBoxItems(){
        return new ArrayList<>(){{
            add(new AbstractMap.SimpleEntry<>(Material.WOODEN_SWORD, getPerParticipantsAlive(1)));
            add(new AbstractMap.SimpleEntry<>(Material.STONE_SWORD, getPerParticipantsAlive(0.7)));
            add(new AbstractMap.SimpleEntry<>(Material.STONE_PICKAXE, getPerParticipantsAlive(0.5)));
            add(new AbstractMap.SimpleEntry<>(Material.CHAINMAIL_BOOTS, getPerParticipantsAlive(1)));
            add(new AbstractMap.SimpleEntry<>(Material.CHAINMAIL_CHESTPLATE, getPerParticipantsAlive(1)));
            add(new AbstractMap.SimpleEntry<>(Material.CHAINMAIL_HELMET, getPerParticipantsAlive(1)));
            add(new AbstractMap.SimpleEntry<>(Material.CHAINMAIL_LEGGINGS, getPerParticipantsAlive(1)));
            add(new AbstractMap.SimpleEntry<>(Material.IRON_SWORD, getPerParticipantsAlive(0.5)));
            add(new AbstractMap.SimpleEntry<>(Material.IRON_BOOTS, getPerParticipantsAlive(0.1)));
            add(new AbstractMap.SimpleEntry<>(Material.IRON_CHESTPLATE, getPerParticipantsAlive(0.2)));
            add(new AbstractMap.SimpleEntry<>(Material.IRON_HELMET, getPerParticipantsAlive(0.1)));
            add(new AbstractMap.SimpleEntry<>(Material.IRON_LEGGINGS, getPerParticipantsAlive(0.1)));
            add(new AbstractMap.SimpleEntry<>(Material.GLASS, getPerParticipantsAlive(32)));
            add(new AbstractMap.SimpleEntry<>(Material.COBWEB, getPerParticipantsAlive(5)));
            add(new AbstractMap.SimpleEntry<>(Material.ENDER_PEARL, getPerParticipantsAlive(4)));
            add(new AbstractMap.SimpleEntry<>(Material.APPLE, getPerParticipantsAlive(1)));
            add(new AbstractMap.SimpleEntry<>(Material.GOLDEN_APPLE, getPerParticipantsAlive(0.4)));
            add(new AbstractMap.SimpleEntry<>(Material.COOKED_PORKCHOP, getPerParticipantsAlive(1)));
            add(new AbstractMap.SimpleEntry<>(Material.COOKED_BEEF, getPerParticipantsAlive(5)));
        }};
    }
    private Integer getPerParticipantsAlive(double d){
        return Math.max(1,(int)Math.round(ChaosPlayer.getChaosPlayerSetByType(PlayerType.PARTICIPANT).size() * d));
    }
}
