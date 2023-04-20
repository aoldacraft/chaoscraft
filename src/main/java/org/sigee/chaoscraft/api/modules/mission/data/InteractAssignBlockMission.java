package org.sigee.chaoscraft.api.modules.mission.data;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.sigee.chaoscraft.utils.LanguageUtil;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

import java.util.Objects;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : InteractAssignBlockMission
 * @since : 2023/04/19
 */
public class InteractAssignBlockMission extends CMissionImpl{

    Material assignMaterial = Material.LECTERN;

    public InteractAssignBlockMission(int ord) {
        super(ord);
        this.point = 5;
    }

    @Override
    public String getTitle(Player player) {
        return "%s %s".formatted(
                LanguageUtil.get(player, ScriptCode.INTERACT_BLOCK_MISSION_TITLE),
                LanguageUtil.get(player, ScriptCode.LECTERN));
    }

    @Override
    public void updateMissionCleared(Player player) {
        this.clearPlayer = player;
    }

    @Override
    public <T> void execEvent(T event) {
        if(!(event instanceof PlayerInteractEvent ev))
            return;
        if(!ev.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if(!Objects.requireNonNull(ev.getClickedBlock()).getType().equals(assignMaterial))
            return;
        this.updateMissionCleared(ev.getPlayer());
    }
}
