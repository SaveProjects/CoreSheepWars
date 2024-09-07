package fr.edminecoreteam.sheepwars.utils.scoreboards;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveScoreboardEvent implements Listener
{
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        Core.getInstance().getScoreboardManager().onLogout(p);
    }
}
