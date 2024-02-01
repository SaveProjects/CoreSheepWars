package fr.edminecoreteam.sheepwars.game;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.game.kits.DefaultKit;
import fr.edminecoreteam.sheepwars.game.spec.Spectate;
import fr.edminecoreteam.sheepwars.waiting.guis.ChooseTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

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
    public void onInteract(PlayerInteractEvent e)
    {
        if (core.isState(State.PREPARATION))
        {
            e.setCancelled(true);
        }
        if (core.isState(State.INGAME))
        {
            if (e.getItem() == null) { return; }

            Player p = e.getPlayer();
            ItemStack it = e.getItem();
            if (it.getType() == Material.IRON_SWORD || it.getType() == Material.ARROW)
            {
                if (core.isState(State.INGAME))
                {
                    DefaultKit defaultKit = new DefaultKit(p);
                    defaultKit.armed();
                }
            }
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e)
    {
        if (core.isState(State.PREPARATION))
        {
            e.setCancelled(true);
        }
        if (core.isState(State.INGAME))
        {
            if (core.isState(State.INGAME))
            {
                Player p = (Player) e.getEntity();
                DefaultKit defaultKit = new DefaultKit(p);
                defaultKit.armed();
            }
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
                    for (Entity entity : p.getNearbyEntities(5, 5, 5))
                    {
                        if (entity instanceof Sheep)
                        {
                            entity.remove();
                        }
                    }
                    p.setHealth(0.0D);
                }
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
                {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent e)
    {
        if (core.isState(State.INGAME) || core.isState(State.PREPARATION))
        {
            if (e.getItemDrop().getItemStack().getType().equals(Material.ARROW) || e.getItemDrop().getItemStack().getType().equals(Material.BOW) || e.getItemDrop().getItemStack().getType().equals(Material.WOOD_SWORD))
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onHungerBarChange(FoodLevelChangeEvent e)
    {
        if (e.getEntityType() != EntityType.PLAYER)
        {
            return;
        }
        if (core.isState(State.INGAME) || core.isState(State.PREPARATION))
        {
            e.setCancelled(true);
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
            e.getDrops().clear();
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
