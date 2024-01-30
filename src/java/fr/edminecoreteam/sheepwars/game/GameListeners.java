package fr.edminecoreteam.sheepwars.game;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.game.kits.DefaultKit;
import fr.edminecoreteam.sheepwars.game.spec.Spectate;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameListeners implements Listener
{
    private final static Core core = Core.getInstance();

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        if (core.isState(State.PREPARATION))
        {
            Player p = e.getPlayer();
            if (!core.teams().getRed().contains(p) && !core.teams().getBlue().contains(p))
            {
                return;
            }

            Location playerLocation = core.spawnListeners().getPlayerLocation(p);
            Location pLoc = p.getLocation();
            if (playerLocation.getX() != pLoc.getX() || playerLocation.getY() != pLoc.getY() || playerLocation.getZ() != pLoc.getZ())
            {
                p.teleport(playerLocation);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerInteractEvent e)
    {
        if (core.isState(State.PREPARATION))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e)
    {
        if (core.isState(State.INGAME))
        {
            if (e.getEntityType() == EntityType.PLAYER)
            {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID))
                {
                    Player p = (Player) e.getEntity();
                    p.setHealth(0.0D);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e)
    {
        if (core.isState(State.INGAME))
        {
            if (e.getEntityType() == EntityType.PLAYER)
            {
                if (e.getDamager().getType() == EntityType.ARROW)
                {
                    Player victim = (Player) e.getEntity();
                    Arrow damage = (Arrow) e.getDamager();
                    Player attacker = (Player) damage.getShooter();

                    attacker.playSound(attacker.getLocation(), Sound.NOTE_PLING, 1.0f, 5.0f);
                    core.titleBuilder().sendActionBar(attacker, "§6Hit - " + victim.getName());
                    DefaultKit defaultKit = new DefaultKit(victim);
                    defaultKit.equip();
                }
                if (e.getDamager().getType() == EntityType.PLAYER)
                {
                    Player victim = (Player) e.getEntity();
                    Player attacker = (Player) e.getDamager();

                    attacker.playSound(attacker.getLocation(), Sound.NOTE_PLING, 1.0f, 5.0f);
                    core.titleBuilder().sendActionBar(attacker, "§6Hit - " + victim.getName());
                    DefaultKit defaultKitVictim = new DefaultKit(victim);
                    defaultKitVictim.equip();

                    DefaultKit defaultKitAttacker = new DefaultKit(attacker);
                    defaultKitAttacker.armed();
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        if (e.getEntityType() == EntityType.PLAYER)
        {
            Player victim = (Player) e.getEntity();
            if (victim.getKiller() != null)
            {
                if (victim.getKiller().getType() == EntityType.PLAYER)
                {
                    Player attacker = (Player) victim.getKiller();

                    attacker.playSound(attacker.getLocation(), Sound.VILLAGER_YES, 1.0f, 5.0f);
                    core.titleBuilder().sendActionBar(attacker, "§6Kill - " + victim.getName());

                    victim.playSound(victim.getLocation(), Sound.VILLAGER_DEATH, 1.0f, 5.0f);

                    if (core.teams().getRed().contains(victim))
                    {
                        e.setDeathMessage("§9" + attacker.getName() + " §fa tué §c" + victim.getName() + "§f.");
                        core.teams().getRedDeath().add(victim.getName());
                    }
                    if (core.teams().getBlue().contains(victim))
                    {
                        e.setDeathMessage("§c" + attacker.getName() + " §fa tué §9" + victim.getName() + "§f.");
                        core.teams().getBlueDeath().add(victim.getName());
                    }
                    Spectate spectate = new Spectate(victim);
                    spectate.init();
                }
            } else if (victim.getKiller() == null)
            {
                victim.playSound(victim.getLocation(), Sound.VILLAGER_DEATH, 1.0f, 5.0f);

                if (core.teams().getRed().contains(victim))
                {
                    e.setDeathMessage("§c" + victim.getName() + " §fest mort(e).");
                    core.teams().getRedDeath().add(victim.getName());
                }
                if (core.teams().getBlue().contains(victim))
                {
                    e.setDeathMessage("§9" + victim.getName() + " §fest mort(e).");
                    core.teams().getBlueDeath().add(victim.getName());
                }
                Spectate spectate = new Spectate(victim);
                spectate.init();
            }
        }
    }
}
