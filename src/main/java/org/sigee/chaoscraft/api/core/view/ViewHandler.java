package org.sigee.chaoscraft.api.core.view;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.sigee.chaoscraft.ChaosCraft;
import org.sigee.chaoscraft.api.Game;
import org.sigee.chaoscraft.api.core.view.entities.ScoreBoardable;
import org.sigee.chaoscraft.api.core.view.entities.ShowType;
import org.sigee.chaoscraft.api.core.view.entities.ViewTask;
import org.sigee.chaoscraft.api.entities.ChaosPlayer;
import org.sigee.chaoscraft.api.entities.enums.PlayerType;
import org.sigee.chaoscraft.utils.MessageUtil;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : ViewHandler
 * @since : 2023/04/18
 */
public class ViewHandler {

    private ViewHandler() {
        createViewScheduler();
    }
    private static final class InstanceHolder {
        private static final ViewHandler instance = new ViewHandler();
    }
    public static ViewHandler getInstance() {
        return ViewHandler.InstanceHolder.instance;
    }
    public static int curTime = 0; // 1 == 0.1sec
    private final ViewData localDB = new ViewData();

    private void createViewScheduler(){
        new BukkitRunnable(){
            @Override
            public void run() {
                curTime += 1;
                excuteViewTask(curTime);
                if(curTime % 10 == 0){
                    updateUserScoreboards();
                }
            }
        }.runTaskTimer(ChaosCraft.getInstance(), 0, 2);
    }

    private void excuteViewTask(int time){
        if(localDB.getViewTask() == null || localDB.getViewTask().getStartTime() > time)
            return;
        ViewTask task = localDB.getAndPopTask();
        task.executeTask();
    }

    public void addViewTask(int laterTime, ShowType type, Player[] players, String msg) {localDB.addViewTask(new ViewTask(curTime + laterTime, type, players, msg));}

    public void stopScoreboardData(){for(int i = 0; i < 5; i++) localDB.clearNumOfScoreboard(i); ChaosPlayer.getChaosPlayers().forEach(participant -> {participant.getPlayer().setScoreboard(localDB.scoreboardManager.getMainScoreboard());});}
    public void addScoreboardData(int num, AbstractMap.SimpleEntry<String, String> data){localDB.registerStringScoreboard(num,data);}
    public void addScoreboardData(int num, ScoreBoardable data){localDB.registerStringScoreboard(num,data);}

    public void updateScoreboardData(int num, int code, AbstractMap.SimpleEntry<String, String> data){localDB.updateScoreboardData(num,code,data);}
    public void updateScoreboardData(int num, int code, ScoreBoardable data){localDB.updateScoreboardData(num,code,data);}

    public void clearScoreboardData(int num){localDB.clearNumOfScoreboard(num);}

    private void updateUserScoreboards(){ChaosPlayer.getChaosPlayers().forEach(this::updateUserScoreboard);}
    private void updateUserScoreboard(ChaosPlayer chaosPlayer){
        int cnt = 0;
        Scoreboard scoreboard = localDB.scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(chaosPlayer.getPlayer().getName(), "", ChatColor.DARK_AQUA + "Chaos Craft");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(ChatColor.DARK_GRAY + "==================").setScore(cnt--);
        objective.getScore( "게임 상태: " + ChatColor.GREEN + "%s".formatted(Game.getInstance().getGameState().name())).setScore(cnt--);
        //objective.getScore( "게임 타임: " + ChatColor.GREEN + "%dsec".formatted(curTime/10)).setScore(cnt--);
        objective.getScore("유저 역할: " + ChatColor.GREEN + "%s".formatted(chaosPlayer.getType().name())).setScore(cnt--);
        objective.getScore(ChatColor.DARK_GRAY + "=================-").setScore(cnt--);

        for(int i = 0; i < 5; i++){
            ArrayList<AbstractMap.SimpleEntry<String,String>> data = localDB.getNumOfScoreboard(i);
            ArrayList<ScoreBoardable> data2 = localDB.getNumOfScoreboard_Runable(i);
            if(data2.size() == 0 && data.size() == 0)
                continue;

            for(AbstractMap.SimpleEntry<String,String> pair : data){
                objective.getScore("%s %s".formatted(pair.getKey(),pair.getValue())).setScore(cnt--);
            }
            for(ScoreBoardable scoreBoardable : data2){
                objective.getScore("%s".formatted(scoreBoardable.getMessage(chaosPlayer.getPlayer()))).setScore(cnt--);
            }
            objective.getScore(ChatColor.DARK_GRAY + "================%d".formatted(i)).setScore(cnt--);
        }
        objective.getScore("현재 유저: %d명".formatted(ChaosPlayer.getChaosPlayerSetByType(PlayerType.PARTICIPANT).size())).setScore(cnt--);
        chaosPlayer.getPlayer().setScoreboard(scoreboard);
    }

    public void playSoundAllParticipants(Sound sound){
        ChaosPlayer.getChaosPlayers().stream().map(ChaosPlayer::getPlayer).forEach(p -> {
            p.playSound(p.getLocation(), sound,1,1);
        });
    }
}
