package fr.edminecoreteam.sheepwars.game.teams.displayname;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatTeam implements Listener
{

	private static Core core = Core.getInstance();
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if (core.isState(State.WAITING) || core.isState(State.STARTING) || core.isState(State.FINISH))
		{
			e.setCancelled(true);
			if (!core.teams().getBlue().contains(p) && !core.teams().getRed().contains(p))
			{
				core.getServer().broadcastMessage("§7" + p.getName() + " §8» §f" + e.getMessage());
			}

			if (core.teams().getBlue().contains(p))
			{
				core.getServer().broadcastMessage("§9§lBLEUE. §9" + p.getName() + " §8» §f" + e.getMessage());
			}
			if (core.teams().getRed().contains(p))
			{
				core.getServer().broadcastMessage("§c§lROUGE. §c" + p.getName() + " §8» §f" + e.getMessage());
			}
		}

		if (core.isState(State.INGAME) || core.isState(State.PREPARATION))
		{
			if (!core.teams().getBlue().contains(p) && !core.teams().getRed().contains(p))
			{
				e.setCancelled(true);
				for (Player pls : core.getServer().getOnlinePlayers())
				{
					if (!core.teams().getBlue().contains(pls) && !core.teams().getRed().contains(pls))
					{
						pls.sendMessage("§8§lSPEC. §7" + p.getName() + " §8» §f" + e.getMessage());
					}
				}
			}
			if (core.teams().getBlue().contains(p))
			{
				e.setCancelled(true);
				String message = e.getMessage();
				if (!message.isEmpty())
				{
					if (message.startsWith("!") || message.startsWith("@"))
					{
						message = message.substring(1);
						core.getServer().broadcastMessage("§7§lGÉNÉRAL §f┃ §9§lBLEUE. §9" + p.getName() + " §8» §f" + e.getMessage());
					}
					else
					{
						for (Player pls : core.teams().getBlue())
						{
							pls.sendMessage("§7§lÉQUIPE §f┃ §9§lBLEUE. §9" + p.getName() + " §8» §f" + e.getMessage());
						}
					}
				}
			}
			if (core.teams().getRed().contains(p))
			{
				e.setCancelled(true);
				String message = e.getMessage();
				if (!message.isEmpty())
				{
					if (message.startsWith("!") || message.startsWith("@"))
					{
						message = message.substring(1);
						core.getServer().broadcastMessage("§7§lGÉNÉRAL §f┃ §c§lROUGE. §c" + p.getName() + " §8» §f" + e.getMessage());
					}
					else
					{
						for (Player pls : core.teams().getRed())
						{
							pls.sendMessage("§7§lÉQUIPE §f┃ §c§lROUGE. §c" + p.getName() + " §8» §f" + e.getMessage());
						}
					}
				}
			}
		}
	}
}
