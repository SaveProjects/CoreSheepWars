package fr.edminecoreteam.sheepwars.waiting.guis;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.utils.minecraft.SkullNBT;
import fr.edminecoreteam.sheepwars.waiting.items.ItemsWaiting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ChooseTeam implements Listener
{
    private ItemStack getSkull(String url) { return SkullNBT.getSkull(url); }
    private static final Core core = Core.getInstance();

    @EventHandler
    public void chooseTeamInteract(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) { return; }

        Player p = (Player) e.getWhoClicked();
        ItemStack it = e.getCurrentItem();
        if (e.getView().getTopInventory().getTitle().equals("§8Choix de votre équipe"))
        {
            if (it.getType() == Material.SKULL_ITEM && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§fÉquipe Aléatoire"))
            {
                e.setCancelled(true);
                core.teams().leaveTeam(p);
                p.playSound(p.getLocation(), Sound.CLICK, 1.0f, 1.0f);
                ItemsWaiting itemsWaiting = new ItemsWaiting(p);
                itemsWaiting.changeTeam("random");
                return;
            }
            if (it.getType() == Material.BANNER && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cÉquipe Rouge"))
            {
                e.setCancelled(true);
                if(!core.teams().getRed().contains(p))
                {
                    if (core.teams().getRed().size() < core.getConfig().getInt("teams.red.players"))
                    {
                        core.teams().leaveTeam(p);
                        core.teams().joinTeam(p, "red");
                        ItemsWaiting itemsWaiting = new ItemsWaiting(p);
                        itemsWaiting.changeTeam("red");
                        return;
                    }
                }
                else
                {
                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
            }

            if (it.getType() == Material.BANNER && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9Équipe Bleue"))
            {
                e.setCancelled(true);
                if(!core.teams().getBlue().contains(p))
                {
                    if (core.teams().getBlue().size() < core.getConfig().getInt("teams.blue.players"))
                    {
                        core.teams().leaveTeam(p);
                        core.teams().joinTeam(p, "blue");
                        ItemsWaiting itemsWaiting = new ItemsWaiting(p);
                        itemsWaiting.changeTeam("blue");
                        return;
                    }
                }
                else
                {
                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
            }
        }
    }

    public void gui(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 54, "§8Choix de votre équipe");
        new BukkitRunnable() {
            int t = 0;
            public void run() {

                if (!p.getOpenInventory().getTitle().equalsIgnoreCase("§8Choix de votre équipe")) { cancel(); }

                    if (!core.teams().getRed().contains(p) && !core.teams().getBlue().contains(p))
                    {
                        ItemStack deco = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)0);
                        ItemMeta decoM = deco.getItemMeta();
                        decoM.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                        decoM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                        decoM.setDisplayName("§r");
                        deco.setItemMeta(decoM);
                        inv.setItem(0, deco); inv.setItem(8, deco); inv.setItem(9, deco); inv.setItem(17, deco);
                        inv.setItem(45, deco); inv.setItem(53, deco); inv.setItem(36, deco); inv.setItem(44, deco);
                    }

                if (core.teams().getRed().contains(p))
                {
                    ItemStack deco = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
                    ItemMeta decoM = deco.getItemMeta();
                    decoM.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    decoM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                    decoM.setDisplayName("§r");
                    deco.setItemMeta(decoM);
                    inv.setItem(0, deco); inv.setItem(8, deco); inv.setItem(9, deco); inv.setItem(17, deco);
                    inv.setItem(45, deco); inv.setItem(53, deco); inv.setItem(36, deco); inv.setItem(44, deco);
                }

                if (core.teams().getBlue().contains(p))
                {
                    ItemStack deco = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)11);
                    ItemMeta decoM = deco.getItemMeta();
                    decoM.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    decoM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                    decoM.setDisplayName("§r");
                    deco.setItemMeta(decoM);
                    inv.setItem(0, deco); inv.setItem(8, deco); inv.setItem(9, deco); inv.setItem(17, deco);
                    inv.setItem(45, deco); inv.setItem(53, deco); inv.setItem(36, deco); inv.setItem(44, deco);
                }

                ItemStack randomTeam = getSkull("http://textures.minecraft.net/texture/7881cc2747ba72cbcb06c3cc331742cd9de271a5bbffd0ecb14f1c6a8b69bc9e");
                ItemMeta randomTeamM = randomTeam.getItemMeta();
                randomTeamM.setDisplayName("§fÉquipe Aléatoire");
                ArrayList<String> loreRandom = new ArrayList<String>();
                loreRandom.add("");
                loreRandom.add(" §dInformation:");
                loreRandom.add(" §f▶ §7Activez ou non le mode");
                loreRandom.add(" §f▶ §7§lÉquipe Aléatoire§7.");
                loreRandom.add("");
                loreRandom.add("§8➡ §fCliquez pour y accéder.");
                randomTeamM.setLore(loreRandom);
                randomTeam.setItemMeta(randomTeamM);
                inv.setItem(4, randomTeam);




                ItemStack red = new ItemStack(Material.BANNER, 1, (byte)1);
                ItemMeta redM = red.getItemMeta();
                redM.setDisplayName("§cÉquipe Rouge");
                if (core.teams().getRed().contains(p)) {
                    redM.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    redM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                }
                ArrayList<String> loreRed = new ArrayList<String>();
                loreRed.add("");
                loreRed.add(" §dInformation:");
                loreRed.add(" §f▶ §7Joueur(s): §a" + core.teams().getRed().size() + "§f/" + "§a" + core.getConfig().getInt("teams.red.players"));
                loreRed.add("");
                loreRed.add(" §bEmplacement(s):");
                if (core.teams().getRed().size() == 0) {
                    loreRed.add(" §f▶ §8Vide...");
                }
                for (Player pls : core.teams().getRed()) {
                    loreRed.add(" §f▶ §8" + pls.getName());
                }
                if (!core.teams().getRed().contains(p))
                {
                    loreRed.add("");
                    loreRed.add("§8➡ §fCliquez pour rejoindre.");
                }
                else
                {
                    loreRed.add("");
                    loreRed.add("§8➡ §aÉquipe sélectionnée.");
                }
                redM.setLore(loreRed);
                red.setItemMeta(redM);
                inv.setItem(21, red);




                ItemStack blue = new ItemStack(Material.BANNER, 1, (byte)4);
                ItemMeta blueM = blue.getItemMeta();
                blueM.setDisplayName("§9Équipe Bleue");
                if (core.teams().getBlue().contains(p)) {
                    blueM.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    blueM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                }
                ArrayList<String> loreBlue = new ArrayList<String>();
                loreBlue.add("");
                loreBlue.add(" §dInformation:");
                loreBlue.add(" §f▶ §7Joueur(s): §a" + core.teams().getBlue().size() + "§f/" + "§a" + core.getConfig().getInt("teams.blue.players"));
                loreBlue.add("");
                loreBlue.add(" §bEmplacement(s):");
                if (core.teams().getBlue().size() == 0) {
                    loreBlue.add(" §f▶ §8Vide...");
                }
                for (Player pls : core.teams().getBlue()) {
                    loreBlue.add(" §f▶ §8" + pls.getName());
                }
                if (!core.teams().getBlue().contains(p))
                {
                    loreBlue.add("");
                    loreBlue.add("§8➡ §fCliquez pour rejoindre.");
                }
                else
                {
                    loreBlue.add("");
                    loreBlue.add("§8➡ §aÉquipe sélectionnée.");
                }
                blueM.setLore(loreBlue);
                blue.setItemMeta(blueM);
                inv.setItem(23, blue);

                ++t;
                if (t == 10) {
                    run();
                }
            }
        }.runTaskTimer((Plugin) core, 0L, 10L);

        p.openInventory(inv);
    }

}
