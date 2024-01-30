package fr.edminecoreteam.sheepwars.game.kits;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class DefaultKit
{
    private final static Core core = Core.getInstance();

    private final Player p;

    public DefaultKit(Player p)
    {
        this.p = p;
    }

    public void equip()
    {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta helmetM = (LeatherArmorMeta) helmet.getItemMeta();

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta chestplateM = (LeatherArmorMeta) chestplate.getItemMeta();

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta leggingsM = (LeatherArmorMeta) leggings.getItemMeta();

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta bootsM = (LeatherArmorMeta) boots.getItemMeta();

        if (core.teams().getRed().contains(p))
        {
            helmetM.setColor(Color.fromRGB(203, 59, 59));
            chestplateM.setColor(Color.fromRGB(203, 59, 59));
            leggingsM.setColor(Color.fromRGB(203, 59, 59));
            bootsM.setColor(Color.fromRGB(203, 59, 59));
        }
        if (core.teams().getBlue().contains(p))
        {
            helmetM.setColor(Color.fromRGB(59, 102, 203));
            chestplateM.setColor(Color.fromRGB(59, 102, 203));
            leggingsM.setColor(Color.fromRGB(59, 102, 203));
            bootsM.setColor(Color.fromRGB(59, 102, 203));
        }

        helmet.setItemMeta(helmetM);
        chestplate.setItemMeta(chestplateM);
        leggings.setItemMeta(leggingsM);
        boots.setItemMeta(bootsM);

        p.getEquipment().setHelmet(helmet);
        p.getEquipment().setChestplate(chestplate);
        p.getEquipment().setLeggings(leggings);
        p.getEquipment().setBoots(boots);
    }

    public void armed()
    {
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta bowM = bow.getItemMeta();
        bowM.setDisplayName("§aArc");
        bowM.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        bowM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bow.setItemMeta(bowM);
        p.getInventory().setItem(0, bow);

        ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
        ItemMeta swordM = sword.getItemMeta();
        swordM.setDisplayName("§aÉpée en bois");
        sword.setItemMeta(swordM);
        p.getInventory().setItem(1, sword);

        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        ItemMeta arrowM = arrow.getItemMeta();
        arrowM.setDisplayName("§aFlèche");
        arrow.setItemMeta(arrowM);
        p.getInventory().setItem(9, arrow);
    }
}
