package fr.edminecoreteam.sheepwars.listeners.connection;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener
{
    private final static Core core = Core.getInstance();

    @EventHandler
    public void event(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        if (core.isState(State.WAITING))
        {
            if (core.getPlayersInGame().contains(p.getName()))
            {
                core.getPlayersInGame().remove(p.getName());
                int finalcount = core.getServer().getOnlinePlayers().size() - 1;
                core.getServer().broadcastMessage("§e" + p.getName() + "§7 a quitté le jeu. §d" + finalcount + "§d/" + core.getMaxplayers());

                core.teams().leaveTeam(p);
            }
        }
        if (core.isState(State.STARTING))
        {
            if (core.getPlayersInGame().contains(p.getName()))
            {
                core.getPlayersInGame().remove(p.getName());
                int finalcount = core.getServer().getOnlinePlayers().size() - 1;
                core.getServer().broadcastMessage("§e" + p.getName() + "§7 a quitté le jeu. §d" + finalcount + "§d/" + core.getMaxplayers());

                core.teams().leaveTeam(p);
            }
        }
        if (core.isState(State.INGAME) || core.isState(State.PREPARATION))
        {
            if (core.teams().getRed().contains(p))
            {
                core.teams().getRed().remove(p);
                core.getPlayersInGame().remove(p.getName());
                int finalcount = core.getServer().getOnlinePlayers().size() - 1;
                if (core.getConfig().getString("type").equalsIgnoreCase("ranked"))
                {
                    core.getServer().broadcastMessage("§c" + p.getName() + "§7 a quitté le jeu classé, il est donc banni(e) du jeu.");
                }
                else if (core.getConfig().getString("type").equalsIgnoreCase("unranked"))
                {
                    if (!core.teams().getRedDeath().contains(p.getName()))
                    {
                        core.getServer().broadcastMessage("§c" + p.getName() + "§7 a quitté le jeu, il peut revenir a tout moment.");
                        core.teams().getRedComeBack().add(p.getName());
                    }
                    else
                    {
                        core.getServer().broadcastMessage("§c" + p.getName() + "§7 a quitté le jeu.");
                        core.teams().getRedDeath().remove(p.getName());
                    }
                }
            }
            if (core.teams().getBlue().contains(p))
            {
                core.teams().getBlue().remove(p);
                core.getPlayersInGame().remove(p.getName());
                int finalcount = core.getServer().getOnlinePlayers().size() - 1;
                if (core.getConfig().getString("type").equalsIgnoreCase("ranked"))
                {
                    core.getServer().broadcastMessage("§9" + p.getName() + "§7 a quitté le jeu classé, il est donc banni(e) du jeu.");
                }
                else if (core.getConfig().getString("type").equalsIgnoreCase("unranked"))
                {
                    if (!core.teams().getBlueDeath().contains(p.getName()))
                    {
                        core.getServer().broadcastMessage("§9" + p.getName() + "§7 a quitté le jeu, il peut revenir a tout moment.");
                        core.teams().getBlueComeBack().add(p.getName());
                    }
                    else
                    {
                        core.getServer().broadcastMessage("§9" + p.getName() + "§7 a quitté le jeu.");
                        core.teams().getBlueDeath().remove(p.getName());
                    }
                }
            }
        }
    }
}
