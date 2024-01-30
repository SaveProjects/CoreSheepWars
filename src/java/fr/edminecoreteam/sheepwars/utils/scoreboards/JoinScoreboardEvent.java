package fr.edminecoreteam.sheepwars.utils.scoreboards;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinScoreboardEvent implements Listener
{
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        Core.getInstance().getScoreboardManager().onLogin(p);
    }
}
