package fr.edminecoreteam.sheepwars.waiting.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemsWaiting
{
    private final Player p;

    public ItemsWaiting(Player p)
    {
        this.p = p;
    }

    public void get()
    {
        ItemStack team = new ItemStack(Material.BANNER, 1, (short)15);
        ItemMeta teamM = team.getItemMeta();
        teamM.setDisplayName("§f§lChoix de votre équipe §7• Clique");
        team.setItemMeta(teamM);
        p.getInventory().setItem(0, team);

        ItemStack leave = new ItemStack(Material.BED, 1);
        ItemMeta leaveM = leave.getItemMeta();
        leaveM.setDisplayName("§c§lQuitter §7• Clique");
        leave.setItemMeta(leaveM);
        p.getInventory().setItem(8, leave);
    }

    public void changeTeam(String teams) {
        if (teams.equalsIgnoreCase("random")) {
            ItemStack team = new ItemStack(Material.BANNER, 1, (short) 15);
            ItemMeta teamM = team.getItemMeta();
            teamM.setDisplayName("§f§lChoix de votre équipe §7• Clique");
            team.setItemMeta(teamM);
            p.getInventory().setItem(0, team);
        }
        if (teams.equalsIgnoreCase("red")) {
            ItemStack team = new ItemStack(Material.BANNER, 1, (short) 1);
            ItemMeta teamM = team.getItemMeta();
            teamM.setDisplayName("§c§lChoix de votre équipe §7• Clique");
            team.setItemMeta(teamM);
            p.getInventory().setItem(0, team);
        }
        if (teams.equalsIgnoreCase("blue")) {
            ItemStack team = new ItemStack(Material.BANNER, 1, (short) 4);
            ItemMeta teamM = team.getItemMeta();
            teamM.setDisplayName("§9§lChoix de votre équipe §7• Clique");
            team.setItemMeta(teamM);
            p.getInventory().setItem(0, team);
        }
    }

    public void clearinv()
    {
        p.getInventory().clear();
    }
}
