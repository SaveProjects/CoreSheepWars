package fr.edminecoreteam.sheepwars.utils.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SpawnListeners
{
    private HashMap<Player, Location> emplacementsJoueurs = new HashMap<Player, Location>();
    public Location getPlayerLocation(Player p)
    {
        return emplacementsJoueurs.get(p);
    }
    public void setPlayerLocation(Player p, Location location)
    {
        emplacementsJoueurs.put(p, location);
    }

    public void teleportPlayerLocation(Player p)
    {
        p.teleport(emplacementsJoueurs.get(p));
    }
}
