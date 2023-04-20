package org.sigee.chaoscraft.api.modules.mission;

import org.sigee.chaoscraft.api.Game;
import org.sigee.chaoscraft.api.core.view.ViewHandler;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.api.entities.enums.PlayerType;
import org.sigee.chaoscraft.api.modules.mission.data.CMission;
import org.sigee.chaoscraft.api.modules.mission.data.CMissionImpl;
import org.sigee.chaoscraft.api.modules.mission.data.EatMission;
import org.sigee.chaoscraft.api.modules.mission.data.InteractAssignBlockMission;

import java.util.ArrayList;
import java.util.Random;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : ChaosMission
 * @since : 2023/04/17
 */
public class ChaosMission {
    private ChaosMission() {}
    private final static Integer GameCode = 1;
    public Integer getGameCode() {
        return GameCode;
    }
    private static final class InstanceHolder {
        private static final ChaosMission instance = new ChaosMission();
    }
    private final ArrayList<CMission> missions = new ArrayList<>();
    private final Integer missionKindQuantity = 2;
    public static ChaosMission getInstance() {
        return ChaosMission.InstanceHolder.instance;
    }

    private CMission createMission(int ord, int code) {
        if(code == 0) {
            return new EatMission(ord);
        }
        else if(code == 1) {
            return new InteractAssignBlockMission(ord);
        }
        return new EatMission(ord);
    }
    public void createMissions() {
        int quantity = ChaosPlayer.getChaosPlayerSetByType(PlayerType.PARTICIPANT).size()/2 + 1;
        for(int i = 0; i < quantity; i++) {
            Random random = new Random();
            int r = random.nextInt(missionKindQuantity);
            CMission ms = createMission(i,r);
            missions.add(ms);
            ViewHandler.getInstance()
                    .addScoreboardData(getGameCode(), ms);
        }
    }

    public void refreshMissions() {
        ViewHandler.getInstance()
                .clearScoreboardData(getGameCode());
        createMissions();
    }

    public void validateMissions() {
        long val = missions.stream().filter(m -> !m.isCleard()).count();
        if (val > 0) {
            return;
        }
        refreshMissions();
    }
    public<T> void listenEvent(T event) {
        missions.forEach(ms ->{
            ms.execEvent(event);
        });
    }


}
