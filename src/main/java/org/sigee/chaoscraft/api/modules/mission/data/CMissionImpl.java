package org.sigee.chaoscraft.api.modules.mission.data;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.api.modules.mission.ChaosMission;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : CMissionImpl
 * @since : 2023/04/19
 */
public class CMissionImpl implements CMission{
    protected Player clearPlayer = null;
    protected Integer order = null;
    protected Integer point = null;

    public CMissionImpl(int ord) {
        this.order = ord;
    }

    @Override
    public Boolean isCleard() {
        return clearPlayer != null;
    }

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public final String getTitle(ChaosPlayer player) {
        return getTitle(player.getPlayer());
    }

    @Override
    public final String getClearPlayerName() {
        if (!isCleard()){
            return "None";
        }
        return clearPlayer.getName();
    }

    @Override
    public void updateMissionCleared(Player player) {
        clearPlayer = player;
        ChaosMission.getInstance().validateMissions();
    }

    @Override
    public <T> void execEvent(T event) {
    }

    @Override
    public String getMessage(Player player) {
        if(isCleard()){
            return ChatColor.STRIKETHROUGH + "cleared by" + player.getName();
        }
        return getTitle(player);
    }
}
