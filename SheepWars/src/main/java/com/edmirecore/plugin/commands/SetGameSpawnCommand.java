package com.edmirecore.plugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetGameSpawnCommand implements CommandExecutor {

    private JavaPlugin plugin;

    public SetGameSpawnCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sheepwars") && args.length == 2) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("sheepwars.setgamespawn")) {
                    String team = args[1].toLowerCase();
                    if (team.equals("blue") || team.equals("red")) {
                        Location location = player.getLocation();
                        plugin.getConfig().set("game-spawn." + team, locationToString(location));
                        plugin.saveConfig();
                        player.sendMessage("Spawn for team " + team + " set to your current location.");
                        return true;
                    } else {
                        player.sendMessage("Invalid team. Use 'blue' or 'red'.");
                        return false;
                    }
                } else {
                    player.sendMessage("You do not have permission to use this command.");
                    return false;
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
                return false;
            }
        }
        return false;
    }

    private String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }
}
