package com.gloppasglop.fk.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gloppasglop.fk.Main;
/**
 * Created by christopheroux on 27/08/16.
 */
public class ScoreboardHandler {

    private Scoreboard scoreboard;
    private Objective objective;

    public ScoreboardHandler() {

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("fk","dummy");

        // objective.setDisplayName(ChatColor.DARK_AQUA + "Fallen Kingdoms");
        objective.setDisplayName(ChatColor.DARK_AQUA + getCurrentFormattedDate("hh:mm:ss"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore(ChatColor.WHITE + "Kills:");
        score.setScore(0);

    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void sendPlayerScoreboard(Player p) {
        p.setScoreboard(scoreboard);
    }

    public void updateTime() {
        //Score score = objective.getScore(ChatColor.WHITE + "Time: "+ getCurrentFormattedDate("hh:mm:ss"));
        //score.setScore(-1);
        //objective.setDisplayName(ChatColor.DARK_AQUA + getCurrentFormattedDate("hh:mm:ss"));
        objective.setDisplayName(ChatColor.DARK_AQUA +
                "Day " + String.format("%d", Main.gametime.days()) +
                " - " + String.format("%02d",Main.gametime.minutes()) +
                ":" + String.format("%02d",Main.gametime.seconds()));
    }

    private String getCurrentFormattedDate(String format) {
        Date date = new Date();
        SimpleDateFormat ft =new SimpleDateFormat(format);
        return ft.format(date);
    }
}
