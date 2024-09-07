package fr.edminecoreteam.sheepwars.content.game.spec;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.utils.game.GameUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Spectate
{
    private final static Core core = Core.getInstance();

    private final Player p;

    private final GameUtils gameUtils = new GameUtils();

    public Spectate(Player p)
    {
        this.p = p;
    }

    public void init()
    {
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        p.setAllowFlight(true);
        p.setFlying(true);

        p.getInventory().clear();
        p.setLevel(0);
        p.setFoodLevel(20);
        p.setHealth(20.0);
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(gameUtils.getSpecSpawn());
        ItemStack leave = new ItemStack(Material.BED, 1);
        ItemMeta leaveM = leave.getItemMeta();
        leaveM.setDisplayName("§c§lQuitter §7• Clique");
        leave.setItemMeta(leaveM);
        p.getInventory().setItem(8, leave);
        if (core.getConfig().getString("type").equalsIgnoreCase("ranked"))
        {
            p.sendTitle("§e§lSheepWars", "§7Mode §6Compétitif§8.");
        }
        if (core.getConfig().getString("type").equalsIgnoreCase("unranked"))
        {
            p.sendTitle("§e§lSheepWars", "§7Mode §6Non-Compétitif§8.");
        }
    }
}
