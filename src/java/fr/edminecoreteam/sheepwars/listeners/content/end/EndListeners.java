package fr.edminecoreteam.sheepwars.listeners.content.end;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.content.end.tasks.AutoStop;
import fr.edminecoreteam.sheepwars.utils.game.GameUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EndListeners
{

    private final static Core core = Core.getInstance();

    public void end()
    {
        GameUtils gameUtils = new GameUtils();
        for (Player pls : core.getServer().getOnlinePlayers())
        {
            pls.getEquipment().setHelmet(null);
            pls.getEquipment().setChestplate(null);
            pls.getEquipment().setLeggings(null);
            pls.getEquipment().setBoots(null);
            pls.getInventory().clear();
            pls.setHealth(20);
            pls.setFoodLevel(20);
            pls.teleport(gameUtils.getSpawn());
            pls.setGameMode(GameMode.ADVENTURE);
        }

        core.getServer().broadcastMessage("");
        core.getServer().broadcastMessage("  §d§lCompte rendu:");
        if (core.getConfig().getString("type").equalsIgnoreCase("unranked"))
        {
            core.getServer().broadcastMessage("   §7• §7Mode§7: §6Non-Compétitif");
        }
        if (core.getConfig().getString("type").equalsIgnoreCase("ranked"))
        {
            core.getServer().broadcastMessage("   §7• §7Mode§7: §6Compétitif");
        }

        if (core.teams().getRed().size() == core.teams().getRedDeath().size())
        {
            core.getServer().broadcastMessage("   §7• §7Vitoire: §9Bleues");
            for (Player pls : core.teams().getBlue())
            {
                pls.sendTitle("§e§lVictoire !", "§7Vous avez remporté la partie !");
            }
            for (Player pls : core.teams().getRed())
            {
                pls.sendTitle("§c§lPerdu...", "§7Peut-être une prochaine fois !");
            }
        }
        if (core.teams().getBlue().size() == core.teams().getBlueDeath().size())
        {
            core.getServer().broadcastMessage("   §7• §7Vitoire: §cRouges");
            for (Player pls : core.teams().getRed())
            {
                pls.sendTitle("§e§lVictoire !", "§7Vous avez remporté la partie !");
            }
            for (Player pls : core.teams().getBlue())
            {
                pls.sendTitle("§c§lPerdu...", "§7Peut-être une prochaine fois !");
            }
        }
        core.getServer().broadcastMessage("");
        core.getServer().broadcastMessage(" §8➡ §fVisionnez vos statistiques sur votre profil.");
        core.getServer().broadcastMessage("");

        AutoStop autoStop = new AutoStop(core);
        autoStop.runTaskTimer((Plugin) core, 0L, 20L);
    }

}
