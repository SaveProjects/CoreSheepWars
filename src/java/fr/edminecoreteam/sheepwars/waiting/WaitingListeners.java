package fr.edminecoreteam.sheepwars.waiting;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.utils.game.GameUtils;
import fr.edminecoreteam.sheepwars.waiting.guis.ChooseTeam;
import fr.edminecoreteam.sheepwars.waiting.items.ItemsWaiting;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WaitingListeners implements Listener
{
    private Player p;
    private static final Core core = Core.getInstance();

    public WaitingListeners(Player p)
    {
        this.p = p;
    }
    private final GameUtils gameUtils = new GameUtils();

    public WaitingListeners()
    {
        //null
    }

    public void getWaitingItems()
    {
        ItemsWaiting waiting = new ItemsWaiting(p);
        waiting.clearinv();
        waiting.get();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if (e.getCurrentItem() == null) { return; }

        Player p = (Player)e.getWhoClicked();
        ItemStack it = e.getCurrentItem();
        if (it.getType() == Material.BANNER)
        {
            if (core.isState(State.WAITING) || core.isState(State.STARTING) || core.isState(State.FINISH))
            {
                e.setCancelled(true);
                ChooseTeam teamGui = new ChooseTeam();
                teamGui.gui(p);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if (e.getItem() == null) { return; }

        Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack it = e.getItem();
        if (it.getType() == Material.BANNER && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK || a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK))
        {
            if (core.isState(State.WAITING) || core.isState(State.STARTING) || core.isState(State.FINISH))
            {
                e.setCancelled(true);
                ChooseTeam teamGui = new ChooseTeam();
                teamGui.gui(p);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent e)
    {
        if (core.isState(State.WAITING) || core.isState(State.STARTING) || core.isState(State.FINISH))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e)
    {
        if (e.getEntity().getType() == EntityType.PLAYER)
        {
            if (core.isState(State.WAITING) || core.isState(State.STARTING) || core.isState(State.FINISH))
            {
                e.setCancelled(true);
                if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID))
                {
                    Player p = (Player) e.getEntity();
                    p.teleport(gameUtils.getSpawn());
                }
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
        if (core.isState(State.WAITING) || core.isState(State.STARTING) || core.isState(State.FINISH))
        {
            e.setCancelled(true);
        }
    }
}
