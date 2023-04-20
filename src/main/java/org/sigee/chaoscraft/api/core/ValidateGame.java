package org.sigee.chaoscraft.api.core;

import org.bukkit.entity.Player;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.api.entities.enums.PlayerType;
import org.sigee.chaoscraft.utils.MessageUtil;
import org.sigee.chaoscraft.utils.enums.ScriptCode;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : ValidateGame
 * @since : 2023/04/14
 */
public class ValidateGame {
    private Integer minPlayerQuantity = 1;

    public Boolean updateLoading(Player sender) {
        if(ChaosPlayer.getChaosPlayerSetByType(PlayerType.PARTICIPANT).size() < minPlayerQuantity) {
            MessageUtil.printMsgToApiSender(sender, ScriptCode.LESS_USER);
            return false;
        }

        //TODO 새로운 조건 추가하기.

        return true;
    }

    public Boolean updateStart(Player sender) {

        //TODO 새로운 조건 추가하기.

        return true;
    }

    public Boolean updateFrozen(Player sender) {

        //TODO 새로운 조건 추가하기.

        return true;
    }
}
