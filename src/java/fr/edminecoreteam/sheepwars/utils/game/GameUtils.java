package fr.edminecoreteam.sheepwars.utils.game;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class GameUtils
{
    private static final Core core = Core.getInstance();

    private final Location redSpawn = new Location(Bukkit.getWorld("game"),
            (float) core.getConfig().getDouble("maps." + core.world + ".red.x"),
            (float) core.getConfig().getDouble("maps." + core.world + ".red.y"),
            (float) core.getConfig().getDouble("maps." + core.world + ".red.z"),
            (float) core.getConfig().getDouble("maps." + core.world + ".red.f"),
            (float) core.getConfig().getDouble("maps." + core.world + ".red.t"));

    private final Location blueSpawn = new Location(Bukkit.getWorld("game"),
            (float) core.getConfig().getDouble("maps." + core.world + ".blue.x"),
            (float) core.getConfig().getDouble("maps." + core.world + ".blue.y"),
            (float) core.getConfig().getDouble("maps." + core.world + ".blue.z"),
            (float) core.getConfig().getDouble("maps." + core.world + ".blue.f"),
            (float) core.getConfig().getDouble("maps." + core.world + ".blue.t"));

    private final Location specSpawn = new Location(Bukkit.getWorld("game"),
            (float) core.getConfig().getDouble("maps." + core.world + ".spec.x"),
            (float) core.getConfig().getDouble("maps." + core.world + ".spec.y"),
            (float) core.getConfig().getDouble("maps." + core.world + ".spec.z"),
            (float) core.getConfig().getDouble("maps." + core.world + ".spec.f"),
            (float) core.getConfig().getDouble("maps." + core.world + ".spec.t"));

    private final Location spawn = new Location(Bukkit.getWorld(
            core.getConfig().getString("spawn.world")),
            (float) core.getConfig().getDouble("spawn.x"),
            (float) core.getConfig().getDouble("spawn.y"),
            (float) core.getConfig().getDouble("spawn.z"),
            (float) core.getConfig().getDouble("spawn.f"),
            (float) core.getConfig().getDouble("spawn.t"));

    public Location getRedSpawn() { return this.redSpawn; }
    public Location getBlueSpawn() { return this.blueSpawn; }
    public Location getSpecSpawn() { return this.specSpawn; }
    public Location getSpawn() { return this.spawn; }

    public String convertTime(int timeInSeconds)
    {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02dm %02ds", minutes, seconds);
    }

    public void teleportPlayer(Player p, Location location)
    {
        double randomX = location.getX() + (Math.random() - 1.5) * 10;
        double randomZ = location.getZ() + (Math.random() - 1.5) * 10;

        Location newLocation = new Location(location.getWorld(), randomX, location.getY(), randomZ);

        List<Entity> nearbyEntities = (List<Entity>) newLocation.getWorld().getNearbyEntities(newLocation, 15, 15, 15);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player && !entity.equals(newLocation)) {
                // S'il y a un joueur à proximité, déplacer la destination
                newLocation.add(2, 0, 2); // Déplacer légèrement vers le haut
            }
        }

        // Vérifier si la destination est dans un bloc solide
        if (newLocation.getBlock().getType().isSolid()) {
            // Trouver une position non solide à proximité
            Location safeLocation = newLocation.clone().add(2, 0, 2); // Déplacer légèrement vers le haut
            Block block = safeLocation.getBlock();
            while (block.getType().isSolid()) {
                safeLocation.add(2, 0, 2);
                block = safeLocation.getBlock();
            }
        }
        p.teleport(newLocation);
        core.spawnListeners().setPlayerLocation(p, newLocation);
    }
}
