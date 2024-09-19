package com.edmirecore.plugin;

import com.edmirecore.plugin.commands.SetGameSpawnCommand;
import com.edmirecore.plugin.game.GameManager;
import com.edmirecore.plugin.lobby.LobbyManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    private LobbyManager lobbyManager;
    private GameManager gameManager;
    private int timer = 60;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        lobbyManager = new LobbyManager(this);
        gameManager = new GameManager(this);

        getCommand("sheepwars").setExecutor(new SetGameSpawnCommand(this));

        getServer().getPluginManager().registerEvents(lobbyManager, this);

        new BukkitRunnable() {
            @Override
            public void run() {
                int currentPlayers = getServer().getOnlinePlayers().size();
                int maxPlayers = getConfig().getInt("scoreboard.max_players");

                if (currentPlayers >= (maxPlayers * getConfig().getInt("scoreboard.start_threshold") / 100)) {
                    timer--;
                    lobbyManager.updateTimer(timer);
                }

                if (timer <= 0) {
                    startGame();
                    cancel(); 
                }
            }
        }.runTaskTimer(this, 0, 20); 
    }

    private void startGame() {
        getServer().broadcastMessage("La partie commence !");

        gameManager.startGame();
    }

    @Override
    public void onDisable() {
    }
}
