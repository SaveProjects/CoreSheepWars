package com.edmirecore.plugin.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class GameScoreboard {
    private Scoreboard scoreboard;
    private Objective objective;

    public GameScoreboard() {
        ScoreboardManager manager = (ScoreboardManager) Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("game", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Mode de Jeu");
    }

    public void updateScore(Player player, int score) {
        Score scoreEntry = objective.getScore(player.getName());
        scoreEntry.setScore(score);
        player.setScoreboard(scoreboard);
    }
}
