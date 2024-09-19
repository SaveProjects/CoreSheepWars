package com.edmirecore.plugin.teams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import com.edmirecore.plugin.Main;
import java.util.HashSet;
import java.util.Set;

public class TeamManager {
    private Team redTeam;
    private Team blueTeam;
    private Scoreboard scoreboard;
    private Set<Player> redPlayers;
    private Set<Player> bluePlayers;

    public TeamManager(Main plugin) {
        redTeam = scoreboard.registerNewTeam("Red");
        blueTeam = scoreboard.registerNewTeam("Blue");

        redTeam.setPrefix(ChatColor.RED.toString());
        blueTeam.setPrefix(ChatColor.BLUE.toString());

        redPlayers = new HashSet<>();
        bluePlayers = new HashSet<>();
    }

    public void addPlayerToTeam(Player player, String teamName) {
        if (teamName.equalsIgnoreCase("red")) {
            redTeam.addEntry(player.getName());
            redPlayers.add(player);
        } else {
            blueTeam.addEntry(player.getName());
            bluePlayers.add(player);
        }
        player.setScoreboard(scoreboard);
    }
}
