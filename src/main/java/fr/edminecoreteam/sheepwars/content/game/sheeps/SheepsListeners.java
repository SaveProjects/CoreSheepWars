package fr.edminecoreteam.sheepwars.content.game.sheeps;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class SheepsListeners implements Listener
{
    private final static Core core = Core.getInstance();

    @EventHandler
    private void onSheepDeath(EntityDeathEvent e)
    {
        if (e.getEntityType() == EntityType.SHEEP)
        {
            e.getDrops().clear();
        }
    }

    @EventHandler
    private void onSheepDamage(EntityDamageEvent e)
    {
        if (e.getEntityType() == EntityType.SHEEP)
        {
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE)
            {
                e.setCancelled(true);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL)
            {
                e.setCancelled(true);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.POISON)
            {
                e.setCancelled(true);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
            {
                e.setCancelled(true);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            {
                e.setCancelled(true);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.THORNS)
            {
                e.setCancelled(true);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
            {
                e.setCancelled(true);
            }
        }
    }
}
