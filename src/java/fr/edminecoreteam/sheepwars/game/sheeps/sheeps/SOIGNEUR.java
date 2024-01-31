package fr.edminecoreteam.sheepwars.game.sheeps.sheeps;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.utils.game.GameUtils;
import fr.edminecoreteam.sheepwars.utils.minecraft.SkullNBT;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SOIGNEUR implements Listener
{
    private final GameUtils gameUtils = new GameUtils();
    private ItemStack getSkull(String url) { return SkullNBT.getSkull(url); }
    private final static Core core = Core.getInstance();
    private final String url = "http://textures.minecraft.net/texture/2e7cf1c58dbb7c3255b94c6043fa8f0d776c134f4d98b81ca31410965f47a25a";
    private final ItemStack block = getSkull(url);
    private final DyeColor sheepColor = DyeColor.PINK;
    private final String name = "§dMouton Soigneur";

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
                    }
                    p.getInventory().remove(p.getItemInHand());
                    Sheep sheep = (Sheep) p.getWorld().spawnEntity(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ()), EntityType.SHEEP);
                    sheep.setColor(sheepColor);
                    sheep.setCustomName(name);
                    sheep.setCustomNameVisible(false);
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
            public void run() {

                ++t;
                ++f;

                if (sheep == null || sheep.isDead())
                {
                    cancel();
                }

                gameUtils.hearthParticles(sheep);
                Location loc = sheep.getLocation();
                for (Player pls : core.getServer().getOnlinePlayers())
                {
                    double distanceSquared = pls.getLocation().distanceSquared(loc);
                    if (distanceSquared <= 15 * 15)
                    {
                        if (sheep.isOnGround())
                        {
                            if (pls.getHealth() < 20)
                            {
                                double health = pls.getHealth();
                                if (pls.getHealth() >= 19)
                                {
                                    pls.setHealth(20);
                                }
                                else
                                {
                                    pls.setHealth(health + 0.5);
                                }
                            }
                        }
                    }
                }

                if (f == 20)
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
