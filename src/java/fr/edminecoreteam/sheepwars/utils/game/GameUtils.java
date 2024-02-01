package fr.edminecoreteam.sheepwars.utils.game;

import fr.edminecoreteam.sheepwars.Core;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class GameUtils
{
    private static final Core core = Core.getInstance();

    private List<Block> replacedBlocks = new ArrayList<>();

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
        double randomX = location.getX() + (Math.random() - 0.5) * 3;
        double randomZ = location.getZ() + (Math.random() - 0.5) * 3;

        Location newLocation = new Location(location.getWorld(), randomX, location.getY(), randomZ);
        p.teleport(newLocation);
        core.spawnListeners().setPlayerLocation(p, newLocation);
    }

    public void createCircleOfFire(Location center) {
        final int RADIUS = 3; // Rayon du cercle
        final int ARC_ANGLE = 360; // Angle de l'arc de cercle (cercle incomplet)
        final int NUM_FIRE = 35; // Nombre de blocs de feu

        // Calculer la position de chaque bloc de feu sur l'arc de cercle
        for (int i = 0; i < NUM_FIRE; i++) {
            double angle = (double) i / NUM_FIRE * ARC_ANGLE;
            double x = center.getX() + RADIUS * Math.cos(Math.toRadians(angle));
            double z = center.getZ() + RADIUS * Math.sin(Math.toRadians(angle));
            Location fireLocation = new Location(center.getWorld(), x, center.getY(), z);
            fireLocation.getWorld().getBlockAt(fireLocation).setType(Material.FIRE);
        }

        // Supprimer le feu après un certain délai
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < NUM_FIRE; i++) {
                    double angle = (double) i / NUM_FIRE * ARC_ANGLE;
                    double x = center.getX() + RADIUS * Math.cos(Math.toRadians(angle));
                    double z = center.getZ() + RADIUS * Math.sin(Math.toRadians(angle));
                    Location fireLocation = new Location(center.getWorld(), x, center.getY(), z);
                    fireLocation.getWorld().getBlockAt(fireLocation).setType(Material.AIR);
                }
            }
        }.runTaskLater(core, 20 * 4); // Supprime le feu après 10 secondes (20 ticks/s * 10 s)
    }

    public void createCircleOfIce(Location center) {
        final int RADIUS = 3; // Rayon du cercle
        final int ARC_ANGLE = 180; // Angle de l'arc de cercle (cercle complet)
        final int NUM_ICE = 10; // Nombre de blocs de glace

        // Sauvegarder les blocs remplacés
        List<Block> replacedBlocks = new ArrayList<>();

        // Calculer la position de chaque bloc de glace sur l'arc de cercle
        for (int i = 0; i < NUM_ICE; i++) {
            double angle = (double) i / NUM_ICE * ARC_ANGLE;
            double x = center.getX() + RADIUS * Math.cos(Math.toRadians(angle));
            double z = center.getZ() + RADIUS * Math.sin(Math.toRadians(angle));
            Location iceLocation = new Location(center.getWorld(), x, center.getY() - 1, z);
            Block block = iceLocation.getBlock();
            replacedBlocks.add(block); // Sauvegarder le bloc remplacé
            block.setType(Material.ICE);
        }

        // Supprimer la glace et restaurer les blocs d'origine après un certain délai
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < NUM_ICE; i++) {
                    double angle = (double) i / NUM_ICE * ARC_ANGLE;
                    double x = center.getX() + RADIUS * Math.cos(Math.toRadians(angle));
                    double z = center.getZ() + RADIUS * Math.sin(Math.toRadians(angle));
                    Location iceLocation = new Location(center.getWorld(), x, center.getY(), z);
                    iceLocation.getWorld().getBlockAt(iceLocation).setType(Material.AIR);
                }
                for (Block block : replacedBlocks) {
                    block.setType(block.getType()); // Réinitialiser le type de bloc à son état initial
                }
            }
        }.runTaskLater(core, 20 * 4); // Supprime la glace après 4 secondes (20 ticks/s * 4 s)
    }

    public void summonLightningStorm(Location center) {
        World world = center.getWorld();
        int radius = 8; // Rayon du cercle
        int offsetX = -radius + (int) (Math.random() * (2 * radius + 1)); // Décalage X aléatoire dans le rayon
        int offsetZ = -radius + (int) (Math.random() * (2 * radius + 1)); // Décalage Z aléatoire dans le rayon

        // Position aléatoire dans le rayon autour du centre
        Location randomLocation = center.clone().add(offsetX, 0, offsetZ);
        world.strikeLightning(randomLocation); // Créer un orage à la position aléatoire
    }

    public void explodeSheep(Sheep sheep) {
        Location sheepLocation = sheep.getLocation();
        World world = sheepLocation.getWorld();

        // Jouer le son du sifflement du Creeper
        world.playSound(sheepLocation, Sound.CREEPER_HISS, 1.0f, 1.0f);

        // Lancer une tâche pour reproduire l'explosion après un court délai
        new BukkitRunnable() {
            @Override
            public void run() {
                // Créer une explosion simulée
                world.createExplosion(sheepLocation, 3.0f, true);
                sheep.remove();
            }
        }.runTaskLater(core, 15L); // Délai d'une seconde (20 ticks)
    }

    public void hearthParticles(Sheep sheep)
    {
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.HEART, true,
                (float) sheep.getLocation().getX(), (float) sheep.getLocation().getY() + 1, (float) sheep.getLocation().getZ(), 0, 0, 0, 5, 5);
        for (Player pls : core.getServer().getOnlinePlayers()) {
            ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void fragmentation(Sheep sheep) {
        Location sheepLocation = sheep.getLocation();
        World world = sheepLocation.getWorld();

        // Créer une explosion initiale
        world.createExplosion(sheepLocation, 4.0f, true);

        // Faire apparaître plusieurs moutons explosifs autour de la position du mouton initial
        for (int i = 0; i < 10; i++) {
            double offsetX = -5 + Math.random() * 10; // Décalage aléatoire en x dans un rayon de 5 blocs
            double offsetZ = -5 + Math.random() * 10; // Décalage aléatoire en z dans un rayon de 5 blocs
            Location spawnLocation = sheepLocation.clone().add(offsetX, 0, offsetZ);

            // S'assurer que le bloc est solide et pas d'eau
            Location checkLocation = spawnLocation.clone().subtract(0, 1, 0);
            if (checkLocation.getBlock().getType().isSolid() && checkLocation.getBlock().getType() != Material.WATER) {
                World world1 = spawnLocation.getWorld();
                Sheep explosiveSheep = (Sheep) world1.spawnEntity(spawnLocation, EntityType.SHEEP);
                explosiveSheep.setCustomName("Explosive Sheep");
                explosiveSheep.setCustomNameVisible(true);

                explosiveSheep.setVelocity(spawnLocation.getDirection().multiply(0.5));

                explosiveSheep.getWorld().createExplosion(spawnLocation, 2.0f, false);
                explosiveSheep.remove();
            }
        }
        sheep.remove();
    }

    public void distorsion(Sheep sheep)
    {
        Location sheepLocation = sheep.getLocation();
        World world = sheepLocation.getWorld();
        List<Entity> nearbyEntities = sheep.getNearbyEntities(10, 10, 10);
        Random random = new Random();

        for (int x = -3; x <= 3; x++)
        {
            for (int z = -3; z <= 3; z++)
            {
                Location targetLocation = sheepLocation.clone().add(x, -1, z);
                Block targetBlock = world.getBlockAt(targetLocation);
                targetBlock.setType(Material.AIR); // Creuser le bloc en dessous d'herbe
            }
        }

        for (int i = 0; i < 3; i++)
        {
            int offsetX = (int) (-5 + Math.random() * 10);
            int offsetZ = (int) (-5 + Math.random() * 10);

            Location targetLocation = sheepLocation.clone().add(offsetX, 1, offsetZ);
            Block targetBlock = world.getBlockAt(targetLocation);

            if (targetBlock.getType() == Material.AIR)
            {
                targetBlock.setType(Material.OBSIDIAN);
                targetBlock.setData((byte) 0);
                targetBlock.getState().update(true, false);

                // Appliquer une impulsion vers le haut
                world.createExplosion(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ(), 0, false, false);
            }
        }

        for (Entity entity : nearbyEntities)
        {
            if (entity instanceof Player)
            {
                Player player = (Player) entity;
                Location playerLocation = player.getLocation();

                // Vérifier s'il y a un bloc solide sous le joueur
                Location belowLocation = playerLocation.clone().subtract(0, 1, 0);
                if (belowLocation.getBlock().getType().isSolid())
                {
                    // Générer une nouvelle position aléatoire autour du mouton
                    double offsetX = -5 + random.nextDouble() * 20; // Décalage aléatoire en x dans un rayon de 10 blocs
                    double offsetZ = -5 + random.nextDouble() * 20; // Décalage aléatoire en z dans un rayon de 10 blocs
                    double newX = sheepLocation.getX() + offsetX;
                    double newZ = sheepLocation.getZ() + offsetZ;

                    // Assurer que les nouvelles coordonnées ne sont pas en dehors du monde
                    double maxY = world.getWorldBorder().getSize() / 2;
                    double newY = world.getHighestBlockYAt((int) newX, (int) newZ);

                    // Téléporter le joueur vers la nouvelle position
                    Location newLocation = new Location(world, newX, newY, newZ);
                    player.teleport(newLocation);
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }
            }
        }
    }

    public static void tremblement(Sheep sheep) {
        Location sheepLocation = sheep.getLocation();
        List<Entity> nearbyEntities = sheep.getNearbyEntities(10, 10, 10);
        Random random = new Random();

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                // Calculer la vélocité aléatoire
                double offsetX = -1 + (random.nextDouble() * 0.1); // Valeur aléatoire entre -1 et 1
                double offsetY = 0.5 + (random.nextDouble() * 0.1); // Valeur aléatoire entre 0.5 et 1 pour un petit saut
                double offsetZ = -1 + (random.nextDouble() * 0.1); // Valeur aléatoire entre -1 et 1
                Location playerLocation = player.getLocation();
                double playerX = playerLocation.getX();
                double playerY = playerLocation.getY();
                double playerZ = playerLocation.getZ();

                final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_NORMAL, true,
                        (float) player.getLocation().getX(), (float) player.getLocation().getY(), (float) player.getLocation().getZ(), 0, 0, 0, 5, 5);
                for (Player pls : core.getServer().getOnlinePlayers()) {
                    ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(packet);
                }


                // Appliquer la vélocité aléatoire
                player.setVelocity(new org.bukkit.util.Vector(offsetX, offsetY, offsetZ));
            }
        }
    }

    public void explosif(Sheep sheep) {
        Location sheepLocation = sheep.getLocation();
        World world = sheepLocation.getWorld();
        world.createExplosion(sheepLocation, 4.0f, true);
        sheep.remove();
    }

    public void tsunami(Sheep sheep) {
        Location sheepLocation = sheep.getLocation();
        List<Entity> nearbyEntities = sheep.getNearbyEntities(7, 7, 7);

        // Créer l'animation de la vague d'eau
        new BukkitRunnable() {
            int radius = 7;
            double waveHeight = 1.5;

            @Override
            public void run() {
                for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 32) {
                    double x = sheepLocation.getX() + radius * Math.cos(theta);
                    double z = sheepLocation.getZ() + radius * Math.sin(theta);
                    Location waterLocation = new Location(sheepLocation.getWorld(), x, sheepLocation.getY(), z);
                    for (int i = 0; i < waveHeight; i++) {
                        Block waterBlock = waterLocation.add(0, i, 0).getBlock();
                        waterBlock.setType(Material.WATER);
                        waterBlock.getRelative(0, -1, 0).setType(Material.AIR); // Supprimer le bloc en dessous de l'eau
                        if (waterBlock.getRelative(0, -1, 0).getType() != Material.BEDROCK) {
                            waterBlock.getRelative(0, -1, 0).breakNaturally(); // Casser le bloc en dessous de l'eau
                        }
                    }
                }
                if (waveHeight > 0) {
                    waveHeight -= 0.1;
                    // Appliquer l'effet de poison aux joueurs touchés
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            Location playerLocation = player.getLocation();
                            if (sheepLocation.distance(playerLocation) <= 7) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1));
                            }
                        }
                    }
                } else {
                    // Restaurer les blocs d'air après le passage de la vague
                    for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 32) {
                        double x = sheepLocation.getX() + radius * Math.cos(theta);
                        double z = sheepLocation.getZ() + radius * Math.sin(theta);
                        Location waterLocation = new Location(sheepLocation.getWorld(), x, sheepLocation.getY(), z);
                        for (int i = 0; i < waveHeight; i++) {
                            Block waterBlock = waterLocation.add(0, i, 0).getBlock();
                            if (waterBlock.getType() == Material.WATER) {
                                waterBlock.setType(Material.AIR);
                            }
                        }
                    }
                    cancel();
                }
            }
        }.runTaskTimer(core, 0, 5); // Exécuter toutes les 5 ticks
    }


    public void singularite(Sheep sheep) {
        Location sheepLocation = sheep.getLocation();
        List<Entity> nearbyEntities = sheep.getNearbyEntities(10, 10, 10);

        // Déplacer le mouton vers le vide
        sheep.setVelocity(new org.bukkit.util.Vector(0, -1, 0));

        // Attraction des joueurs autour du mouton
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                Location playerLocation = player.getLocation();
                double distanceToSheep = sheepLocation.distance(playerLocation);

                // Calculer les vecteurs de direction entre le joueur et le mouton
                double dX = sheepLocation.getX() - playerLocation.getX();
                double dY = sheepLocation.getY() - playerLocation.getY();
                double dZ = sheepLocation.getZ() - playerLocation.getZ();

                // Normaliser le vecteur de direction
                double length = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
                double directionX = dX / length;
                double directionY = dY / length;
                double directionZ = dZ / length;

                // Appliquer une force d'attraction aux joueurs autour du mouton
                double forceMagnitude = Math.max(0, (10 - distanceToSheep) / 10); // Plus proche = plus forte attraction
                double attractionX = directionX * forceMagnitude;
                double attractionY = directionY * forceMagnitude;
                double attractionZ = directionZ * forceMagnitude;

                player.setVelocity(new org.bukkit.util.Vector(attractionX, attractionY, attractionZ));
            }
        }
    }

    public void bonzai(Sheep sheep, Core plugin) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (sheep == null || sheep.isDead()) {
                    cancel(); // Annuler la tâche si le mouton est null ou mort
                    return;
                }

                Location sheepLocation = sheep.getLocation();
                List<Entity> nearbyEntities = sheep.getNearbyEntities(5, 5, 5);

                // Trouver un bloc d'air derrière le mouton
                Location airLocation = sheepLocation.clone().subtract(sheepLocation.getDirection().multiply(2));
                Block blockBehindSheep = airLocation.getBlock();

                // Si le bloc derrière le mouton est de l'air, placer un bloc de feuille
                if (blockBehindSheep.getType() == Material.AIR) {
                    blockBehindSheep.setType(Material.LEAVES);
                }

                // Supprimer les blocs de feuilles autour du mouton
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        Location playerLocation = player.getLocation();
                        if (playerLocation.distance(sheepLocation) <= 2) {
                            playerLocation.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        };

        task.runTaskTimer(plugin, 0, 1); // Exécuter toutes les 20 ticks (1 seconde)
    }
}
