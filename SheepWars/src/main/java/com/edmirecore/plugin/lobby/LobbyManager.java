package com.edmirecore.plugin.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyManager implements Listener, CommandExecutor {

    private JavaPlugin plugin;
    private Location spawnLocation;
    private LobbyScoreboard lobbyScoreboard;
    private int timer = 60;

    public LobbyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadSpawnLocation();
        this.lobbyScoreboard = new LobbyScoreboard(plugin.getConfig());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("sheepwars.setspawn")) {
                Location loc = player.getLocation();
                plugin.getConfig().set("spawn", loc);
                plugin.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Point de spawn défini avec succès !");
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande.");
            }
        }
        return false;
    }

    private void loadSpawnLocation() {
        FileConfiguration config = plugin.getConfig();
        if (config.contains("spawn")) {
            double x = config.getDouble("spawn.x");
            double y = config.getDouble("spawn.y");
            double z = config.getDouble("spawn.z");
            float yaw = (float) config.getDouble("spawn.yaw");
            float pitch = (float) config.getDouble("spawn.pitch");
            spawnLocation = new Location(Bukkit.getWorld(config.getString("spawn.world")), x, y, z, yaw, pitch);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(20);

        int currentPlayers = Bukkit.getOnlinePlayers().size();
        int maxPlayers = plugin.getConfig().getInt("scoreboard.max_players");
        lobbyScoreboard.updateScoreboard(player, currentPlayers, maxPlayers, timer);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                event.setCancelled(true);
                if (spawnLocation != null) {
                    player.teleport(spawnLocation);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
        }
    }

    public void updateTimer(int newTimer) {
        this.timer = newTimer;
        for (Player player : Bukkit.getOnlinePlayers()) {
            lobbyScoreboard.updateScoreboard(player, Bukkit.getOnlinePlayers().size(), plugin.getConfig().getInt("scoreboard.max_players"), timer);
        }
    }
}
