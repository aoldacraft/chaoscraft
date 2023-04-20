package org.sigee.chaoscraft.api.modules.mission.data;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.api.modules.mission.ChaosMission;
import org.sigee.chaoscraft.utils.GenRequirementsUtil;
import org.sigee.chaoscraft.utils.LanguageUtil;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

import java.util.AbstractMap;
import java.util.Random;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : EatMission
 * @since : 2023/04/18
 */
public class EatMission extends CMissionImpl {

    private static final Material[] eatMaterials = {
            Material.APPLE,
            Material.GOLDEN_APPLE,
            Material.COOKED_PORKCHOP
    };

    public EatMission(int ord) {
        super(ord);
        this.point = 3;
        this.eatMaterial = eatMaterials[random.nextInt(eatMaterials.length)];

        GenRequirementsUtil.addChangedItem(
                ChaosMission.getInstance().getGameCode(),
                new AbstractMap.SimpleEntry<>(this.eatMaterial,2));
    }

    private Material eatMaterial;
    private Random random = new Random();

    @Override
    public String getTitle(Player player) {
        ScriptCode mat = ScriptCode.valueOf(eatMaterial.name().toUpperCase());
        ScriptCode str = ScriptCode.EAT_MISSION;
        return "%s %s.".formatted(
                LanguageUtil.get(player, str),
                LanguageUtil.get(player, mat));
    }

    @Override
    public void updateMissionCleared(Player player) {
        super.updateMissionCleared(player);
    }

    @Override
    public <T> void execEvent(T event) {
        if(!(event instanceof PlayerItemConsumeEvent ev))
            return;
        if(!((PlayerItemConsumeEvent) event).getItem().getType().equals(eatMaterial))
            return;
        this.updateMissionCleared(ev.getPlayer());
    }
}
