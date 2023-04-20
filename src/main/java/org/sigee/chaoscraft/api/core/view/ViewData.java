package org.sigee.chaoscraft.api.core.view;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.ScoreboardManager;
import org.sigee.chaoscraft.api.core.view.entities.ScoreBoardable;
import org.sigee.chaoscraft.api.core.view.entities.ViewTask;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Please explain the class
 *
 * @author : sigee-min
 * @fileName : ViewData
 * @since : 2023/04/18
 */
public class ViewData {
    public ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private final Queue<ViewTask> viewTaskQueue = new PriorityQueue<>();

    public ViewTask getAndPopTask(){return viewTaskQueue.poll();}
    public ViewTask getViewTask(){return viewTaskQueue.peek();}
    public void addViewTask(ViewTask task){viewTaskQueue.add(task);}

    private ArrayList<ArrayList<AbstractMap.SimpleEntry<String,String>>> scoreboardTemplate = new ArrayList<>();
    private ArrayList<ArrayList<ScoreBoardable>> scoreboardTemplate_Runable = new ArrayList<>();

    private void initScoreboardTemplate(){
        for(int i = 0; i < 10; i++) {
            scoreboardTemplate.add(new ArrayList<>());
            scoreboardTemplate_Runable.add(new ArrayList<>());
        }
    }

    public ViewData() {
        initScoreboardTemplate();
    }

    public void registerStringScoreboard(int num, AbstractMap.SimpleEntry<String,String> data){
        scoreboardTemplate.get(num).add(data);
    }
    public void registerStringScoreboard(int num, ScoreBoardable data){
        scoreboardTemplate_Runable.get(num).add(data);
    }
    public ArrayList<AbstractMap.SimpleEntry<String,String>> getNumOfScoreboard(int num){
        return scoreboardTemplate.get(num);
    }
    public ArrayList<ScoreBoardable> getNumOfScoreboard_Runable(int num){
        return scoreboardTemplate_Runable.get(num);
    }

    public void updateScoreboardData(int num, int code, AbstractMap.SimpleEntry<String,String> data){scoreboardTemplate.get(num).set(code,data);}
    public void updateScoreboardData(int num, int code, ScoreBoardable data){scoreboardTemplate_Runable.get(num).set(code,data);}

    public void clearNumOfScoreboard(int num){
        scoreboardTemplate.get(num).clear();
        scoreboardTemplate_Runable.get(num).clear();
    }
}
