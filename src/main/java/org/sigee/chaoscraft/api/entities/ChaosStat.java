package org.sigee.chaoscraft.api.entities;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.sigee.chaoscraft.utils.MessageUtil;

import java.util.*;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : ChaosAttribute
 * @since : 2023/04/17
 */
public class ChaosStat {

    private static final Map<Attribute, Float> ENABLED_ATTRIBUTES = new TreeMap<>() {{
        put(Attribute.GENERIC_ATTACK_DAMAGE, 1f);
        put(Attribute.GENERIC_ATTACK_SPEED, 0.1f);
        put(Attribute.GENERIC_ATTACK_KNOCKBACK, 0.1f);

        put(Attribute.GENERIC_ARMOR, 0.1f);
        put(Attribute.GENERIC_ARMOR_TOUGHNESS, 0.1f);
        put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 0.1f);

        put(Attribute.GENERIC_MAX_HEALTH, 0.1f);
        put(Attribute.GENERIC_MOVEMENT_SPEED, 0.005f);
    }};

    private static Map<Attribute, Integer> generateDefaultStats() {
        var tmpMap = new TreeMap<Attribute, Integer>(){};
        ENABLED_ATTRIBUTES.keySet().forEach(attribute -> tmpMap.put(attribute, 0));
        return tmpMap;
    }

    private final Map<Attribute, Integer> playerStat = generateDefaultStats();

    public Map<Attribute, Integer> getPlayerStats() {
        return this.playerStat;
    }

    public Integer getPlayerStat(Attribute attribute) {
        return this.playerStat.get(attribute);
    }

    /**
     * Reset player stat and attribute.
     *
     * @param player the player
     */
    public void resetPlayerStat(Player player) {
        ENABLED_ATTRIBUTES.keySet().forEach(attribute -> {
            getPlayerStats().replace(attribute, 0);
            applyStat(player, attribute);
        });
    }

    public void updateStat(Player player, Attribute attribute, Integer val) {
        var res = getPlayerStats().get(attribute) + val;
        getPlayerStats().replace(attribute, res);
        applyStat(player, attribute);
    }

    private void applyStat(Player player, Attribute attribute) {
        if(!ENABLED_ATTRIBUTES.containsKey(attribute)){
            return;
        }
        try {
            var mul = getPlayerStat(attribute);
            var resV = player.getAttribute(attribute).getDefaultValue() + mul * ENABLED_ATTRIBUTES.get(attribute);
            if(Attribute.GENERIC_MOVEMENT_SPEED.equals(attribute)){
                resV = resV * 0.12;
            }
            player.getAttribute(attribute).setBaseValue(resV);
        }
        catch (Exception e) {
            MessageUtil.printConsoleLog(attribute.name() + "is null");
        }
    }

}
