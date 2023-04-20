package org.sigee.chaoscraft.api.modules.mission.data;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.sigee.chaoscraft.api.core.view.entities.ScoreBoardable;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : CMission
 * @since : 2023/04/17
 */
public interface CMission extends ScoreBoardable {

    String getTitle(Player player);
    String getTitle(ChaosPlayer player);
    Boolean isCleard();

    String getClearPlayerName();
    void updateMissionCleared(Player player);
    <T> void execEvent(T event);
}
