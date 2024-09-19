package com.edmirecore.plugin.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {

    private JavaPlugin plugin;
    private List<Player> blueTeam = new ArrayList<>();
    private List<Player> redTeam = new ArrayList<>();
    private Map<String, Location> spawnLocations = new HashMap<>();
    private final Random random = new Random();

    public GameManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadSpawnLocations();
    }

    private void loadSpawnLocations() {
        spawnLocations.put("blue", stringToLocation(plugin.getConfig().getString("game-spawn.blue")));
        spawnLocations.put("red", stringToLocation(plugin.getConfig().getString("game-spawn.red")));
    }

    private Location stringToLocation(String str) {
        String[] parts = str.split(",");
        return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    public void startGame() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        int halfSize = players.size() / 2;
        blueTeam = players.subList(0, halfSize);
        redTeam = players.subList(halfSize, players.size());

        teleportPlayers();
        equipPlayers();
        startArrowReplenishment();
    }

    private void teleportPlayers() {
        Map<Location, List<Player>> occupiedLocations = new HashMap<>();
        for (Player player : blueTeam) {
            Location loc = getRandomLocation("blue", occupiedLocations);
            player.teleport(loc);
            occupiedLocations.computeIfAbsent(loc, k -> new ArrayList<>()).add(player);
        }
        for (Player player : redTeam) {
            Location loc = getRandomLocation("red", occupiedLocations);
            player.teleport(loc);
            occupiedLocations.computeIfAbsent(loc, k -> new ArrayList<>()).add(player);
        }
    }

    private Location getRandomLocation(String team, Map<Location, List<Player>> occupiedLocations) {
        Location spawn = spawnLocations.get(team);
        List<Location> points = new ArrayList<>();
        points.add(spawn.clone().add(0, 0, 1));
        points.add(spawn.clone().add(1, 0, 0));
        points.add(spawn.clone().add(0, 0, -1));
        points.add(spawn.clone().add(-1, 0, 0));

        points.removeAll(occupiedLocations.keySet());

        return points.isEmpty() ? spawn : points.get(random.nextInt(points.size()));
    }

    private void equipPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
            ItemMeta meta = helmet.getItemMeta();
            meta.setDisplayName(player.getScoreboard().getTeam("blue") != null ? "Blue Helmet" : "Red Helmet");
            helmet.setItemMeta(meta);
            player.getInventory().setHelmet(helmet);

            ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            chestplate.setItemMeta(meta);
            player.getInventory().setChestplate(chestplate);

            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
            leggings.setItemMeta(meta);
            player.getInventory().setLeggings(leggings);

            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
            boots.setItemMeta(meta);
            player.getInventory().setBoots(boots);

            ItemStack sword = new ItemStack(Material.WOOD_SWORD);
            player.getInventory().addItem(sword);

            ItemStack bow = new ItemStack(Material.BOW);
            player.getInventory().addItem(bow);

            ItemStack arrows = new ItemStack(Material.ARROW, 8);
            player.getInventory().addItem(arrows);
        }
    }

    private void startArrowReplenishment() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ItemStack arrows = player.getInventory().getItem(2);
                    if (arrows != null && arrows.getType() == Material.ARROW) {
                        arrows.setAmount(arrows.getAmount() + 1);
                        player.getInventory().setItem(2, arrows);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 100);
    }
}
