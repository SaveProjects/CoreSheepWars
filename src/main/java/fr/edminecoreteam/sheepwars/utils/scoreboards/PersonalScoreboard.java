package fr.edminecoreteam.sheepwars.utils.scoreboards;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.utils.game.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class PersonalScoreboard {
	
	private static final Core core = Core.getInstance();
    private final Player player;
    private final UUID uuid;
    private final ObjectiveSign objectiveSign;

    private final GameUtils gameUtils = new GameUtils();
 
    PersonalScoreboard(Player player){
        this.player = player;
        uuid = player.getUniqueId();
        objectiveSign = new ObjectiveSign("sidebar", "Edmine");
 
        reloadData();
        objectiveSign.addReceiver(player);
    }
 
    public void reloadData(){}
 
    public void setLines(String ip){

        objectiveSign.setDisplayName("§8● §6§lSheepWars §8●");
        objectiveSign.setLine(0, "§8");
        objectiveSign.setLine(1, " §f➡ §dInformations:");
    		if (core.isState(State.WAITING) || core.isState(State.STARTING))
    		{
                if (core.isState(State.WAITING))
                {
                    objectiveSign.setLine(2, "  §8• §7Statut: §fAttente...");

                    objectiveSign.setLine(7, "§2");
                    objectiveSign.setLine(8, "  §8• §7Carte: §e" + core.world);
                    objectiveSign.setLine(9, "§3");
                    objectiveSign.setLine(10, " §8➡ " + ip);
                }
                else if (core.isState(State.STARTING))
                {
                    objectiveSign.setLine(2, "  §8• §7Statut: §eLancement");

                    objectiveSign.setLine(6, "§2");
                    objectiveSign.setLine(7, "  §8• §7Début dans: §a" + core.timers + "§as");
                    objectiveSign.setLine(8, "  §8• §7Carte: §e"+ core.world);
                    objectiveSign.setLine(9, "§3");
                    objectiveSign.setLine(10, " §8➡ " + ip);
                }
                objectiveSign.setLine(3, "§1");
                objectiveSign.setLine(4, "  §8• §7Joueurs: §a" + core.getServer().getOnlinePlayers().size() + "/§a" + core.getMaxplayers());
    			if (core.getConfig().getString("type").equalsIgnoreCase("ranked"))
                {
                    objectiveSign.setLine(5, "  §8• §7Mode: §6Compétitif");
                }
                else if (core.getConfig().getString("type").equalsIgnoreCase("unranked"))
                {
                    objectiveSign.setLine(5, "  §8• §7Mode: §eNon-Compétitif");
                }
    		}
            if (core.isState(State.INGAME) || core.isState(State.PREPARATION) || core.isState(State.FINISH))
            {
                if (!core.teams().getRed().contains(player) && !core.teams().getBlue().contains(player))
                {
                    int redAlive = core.teams().getRed().size() - core.teams().getRedDeath().size();
                    int blueAlive = core.teams().getBlue().size() - core.teams().getBlueDeath().size();
                    objectiveSign.setLine(2, "  §8• §c§lROUGES§7: §a" + redAlive + "§a❤");
                    objectiveSign.setLine(3, "  §8• §9§lBLEUES§7: §a" + blueAlive + "§a❤");
                }
                if (core.teams().getRed().contains(player))
                {
                    int redAlive = core.teams().getRed().size() - core.teams().getRedDeath().size();
                    int blueAlive = core.teams().getBlue().size() - core.teams().getBlueDeath().size();
                    objectiveSign.setLine(2, "  §8• §c§lROUGES§7 (vous): §a" + redAlive + "§a❤");
                    objectiveSign.setLine(3, "  §8• §9§lBLEUES§7: §a" + blueAlive + "§a❤");
                }
                if (core.teams().getBlue().contains(player))
                {
                    int redAlive = core.teams().getRed().size() - core.teams().getRedDeath().size();
                    int blueAlive = core.teams().getBlue().size() - core.teams().getBlueDeath().size();
                    objectiveSign.setLine(2, "  §8• §c§lROUGES§7: §a" + redAlive + "§a❤");
                    objectiveSign.setLine(3, "  §8• §9§lBLEUES§7 (vous): §a" + blueAlive + "§a❤");
                }
                objectiveSign.setLine(4, "§5");
                if (core.isState(State.INGAME))
                {
                    objectiveSign.setLine(5, "  §8• §7Temps restant: §b" + gameUtils.convertTime(core.timers));
                }
                if (core.isState(State.PREPARATION))
                {
                    objectiveSign.setLine(5, "  §8• §7Début dans: §a" + core.timers + "§as");
                }
                if (core.isState(State.FINISH))
                {
                    objectiveSign.setLine(5, "  §8• §7Fermeture dans: §6" + core.timers + "§6s");
                }
                objectiveSign.setLine(6, "  §8• §7Carte: §e"+ core.world);
                if (core.getConfig().getString("type").equalsIgnoreCase("ranked"))
                {
                    objectiveSign.setLine(7, "  §8• §7Mode: §6Compétitif");
                }
                else if (core.getConfig().getString("type").equalsIgnoreCase("unranked"))
                {
                    objectiveSign.setLine(7, "  §8• §7Mode: §eNon-Compétitif");
                }
                objectiveSign.setLine(8, "§3");
                objectiveSign.setLine(9, " §8➡ " + ip);
            }
 
        objectiveSign.updateLines();
    }
 
    public void onLogout(){
        objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
    }

    public void fakeLogout(){
        objectiveSign.removeReceiver(player);
    }
}