package fr.edminecoreteam.sheepwars.utils.scoreboards;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeScoreboardEvent implements Listener
{
    @EventHandler
    public void playerWorldChange(PlayerChangedWorldEvent e)
    {
        final Player p = e.getPlayer();
        Core.getInstance().getScoreboardManager().fakeLogout(p);
        Core.getInstance().getScoreboardManager().onLogin(p);
        Core.getInstance().getScoreboardManager().update(p);
    }

}
