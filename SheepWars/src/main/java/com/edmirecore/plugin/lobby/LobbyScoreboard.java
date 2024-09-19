package com.edmirecore.plugin.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class LobbyScoreboard {

    private FileConfiguration config;
    private ScoreboardManager manager;
    private Scoreboard scoreboard;
    private Objective objective;

    public LobbyScoreboard(FileConfiguration config) {
        this.config = config;
        this.manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("SheepWarsLobby", "dummy");

        String title = config.getString("scoreboard.title");
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void updateScoreboard(Player player, int currentPlayers, int maxPlayers, int timer) {
        scoreboard.getEntries().forEach(scoreboard::resetScores);

        // line 1
        String playersLine = config.getString("scoreboard.players")
                .replace("{players}", String.valueOf(currentPlayers))
                .replace("{maxplayer}", String.valueOf(maxPlayers));
        objective.getScore(ChatColor.translateAlternateColorCodes('&', playersLine)).setScore(4);

        // Line 2
        String startStatus;
        if (currentPlayers < (maxPlayers * config.getInt("scoreboard.start_threshold") / 100)) {
            startStatus = config.getString("scoreboard.start_status_waiting");
        } else {
            startStatus = config.getString("scoreboard.start_status_timer").replace("{timer}", String.valueOf(timer));
        }
        objective.getScore(ChatColor.translateAlternateColorCodes('&', startStatus)).setScore(3);

        // Line 3
        String mapLine = config.getString("scoreboard.map").replace("{mapname}", config.getString("scoreboard.mapname"));
        objective.getScore(ChatColor.translateAlternateColorCodes('&', mapLine)).setScore(2);

        // Line 4
        String modeLine = config.getString("scoreboard.mode").replace("{mode}", config.getString("scoreboard.mode"));
        objective.getScore(ChatColor.translateAlternateColorCodes('&', modeLine)).setScore(1);

        // Line 5
        String ipLine = config.getString("scoreboard.ip");
        objective.getScore(ChatColor.translateAlternateColorCodes('&', ipLine)).setScore(0);

        player.setScoreboard(scoreboard);
    }
}
