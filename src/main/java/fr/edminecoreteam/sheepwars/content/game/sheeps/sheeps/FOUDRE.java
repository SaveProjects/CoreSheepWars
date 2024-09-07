package fr.edminecoreteam.sheepwars.content.game.sheeps.sheeps;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.utils.game.GameUtils;
import fr.edminecoreteam.sheepwars.utils.minecraft.SkullNBT;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
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

import java.util.ArrayList;
import java.util.List;

public class FOUDRE implements Listener
{
    private final GameUtils gameUtils = new GameUtils();
    private ItemStack getSkull(String url) { return SkullNBT.getSkull(url); }
    private final static Core core = Core.getInstance();
    private final String url = "http://textures.minecraft.net/texture/12a5354c230e861aac72734a4582d1317026454b807ac353fc3a0bd0d8c422ba";
    private final ItemStack block = getSkull(url);
    private final DyeColor sheepColor = DyeColor.YELLOW;
    private final String name = "§6Mouton De Foudre";

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
                    sheep.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 2 - 1));
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
            final List<Player> playerList = new ArrayList<Player>();
            public void run() {

                ++t;
                ++f;

                if (sheep == null || sheep.isDead())
                {
                    for (Player pls : core.getServer().getOnlinePlayers())
                    {
                        pls.resetPlayerWeather();
                        pls.resetPlayerTime();
                    }
                    cancel();
                }

                if (sheep != null && sheep.isOnGround())
                {
                    gameUtils.summonLightningStorm(sheep.getLocation());
                    Location loc = sheep.getLocation();
                    for (Player pls : core.getServer().getOnlinePlayers())
                    {
                        double distanceSquared = pls.getLocation().distanceSquared(loc);
                        if (distanceSquared <= 10 * 10)
                        {
                            if (sheep.isOnGround())
                            {
                                pls.setPlayerTime(18000, false);
                                pls.setPlayerWeather(WeatherType.DOWNFALL);
                                sheep.setVelocity(pls.getLocation().toVector().subtract(sheep.getLocation().toVector()).normalize().multiply(1));
                                playerList.add(pls);
                            }
                        }
                    }
                }

                if (f == 20)
                {
                    for (Player pls : core.getServer().getOnlinePlayers())
                    {
                        pls.resetPlayerWeather();
                        pls.resetPlayerTime();
                    }
                    sheep.remove();
                    cancel();
                }

                if (t == 1) {
                    run();
                }
            }
        }.runTaskTimer((Plugin) core, 0L, 10L);
    }
}
