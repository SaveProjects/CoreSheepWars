package fr.edminecoreteam.sheepwars.game.sheeps.sheeps;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.utils.game.GameUtils;
import fr.edminecoreteam.sheepwars.utils.minecraft.SkullNBT;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TREMBLEMENT implements Listener
{
    private final GameUtils gameUtils = new GameUtils();
    private ItemStack getSkull(String url) { return SkullNBT.getSkull(url); }
    private final static Core core = Core.getInstance();
    private final String url = "http://textures.minecraft.net/texture/e6c2a2755b20ddff551a6903f2dc7e61f13ebe39b1d5ca929c87bd8583ec801f";
    private final ItemStack block = getSkull(url);
    private final DyeColor sheepColor = DyeColor.GRAY;
    private final String name = "§8Mouton De Tremblement";

    public void get(Player player)
    {
        ItemStack itemStack = block;
        ItemMeta itemStackM = itemStack.getItemMeta();
        itemStackM.setDisplayName(name);
        itemStack.setItemMeta(itemStackM);
        player.getInventory().addItem(itemStack);
        player.sendMessage("§f§lSheepWars §8» §7Vous avez reçu: " + name);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack it = e.getItem();
        if (it == null) { return; }
        if (!core.isState(State.INGAME)) { e.setCancelled(true); return; }

        if (a == Action.LEFT_CLICK_AIR ||
                a == Action.LEFT_CLICK_BLOCK ||
                a == Action.RIGHT_CLICK_AIR ||
                a == Action.RIGHT_CLICK_BLOCK)
        {
            if (it.hasItemMeta())
            {
                String getName = it.getItemMeta().getDisplayName();
                if (getName.equalsIgnoreCase(name))
                {
                    for (Player pls : core.getServer().getOnlinePlayers())
                    {
                        pls.playSound(p.getLocation(), Sound.SHEEP_IDLE, 1.0f, 0.8f);
                        pls.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 0.8f);
                    }
                    p.getInventory().remove(p.getItemInHand());
                    Vector pushDirection = p.getLocation().getDirection().multiply(4);
                    Sheep sheep = (Sheep) p.getWorld().spawnEntity(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ()), EntityType.SHEEP);
                    sheep.setColor(sheepColor);
                    sheep.setCustomName(name);
                    sheep.setCustomNameVisible(false);
                    sheep.setVelocity(pushDirection);
                    init(p, sheep);
                }
            }
        }
    }

    private void init(Player p, Sheep sheep)
    {
        new BukkitRunnable() {
            int t = 0;
            int f = 0;
            int q = 0;
            public void run() {

                ++t;
                ++f;
                ++q;
                if (sheep == null || sheep.isDead())
                {
                    cancel();
                }

                if (q == 3)
                {
                    gameUtils.tremblement(sheep);
                    q = 0;
                }

                if (f == 10)
                {
                    sheep.remove();
                    cancel();
                }

                if (t == 1) {
                    run();
                }
            }
        }.runTaskTimer((Plugin) core, 5L, 10L);
    }
}
