package fr.edminecoreteam.sheepwars.utils.minecraft;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullNBT implements Listener
{
	public static ItemStack getSkull(String url) {
	    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
	    if (url.isEmpty())
	      return head; 
	    SkullMeta headMeta = (SkullMeta)head.getItemMeta();
	    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	    byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { url }).getBytes());
	    profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
	    Field profileField = null;
	    try {
	      profileField = headMeta.getClass().getDeclaredField("profile");
	      profileField.setAccessible(true);
	      profileField.set(headMeta, profile);
	    } catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException e1) {
	      e1.printStackTrace();
	    } 
	    head.setItemMeta((ItemMeta)headMeta);
	    return head;
	  }
}
