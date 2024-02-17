package fr.edminecoreteam.sheepwars.content.game.tasks;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.content.game.Game;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Start extends BukkitRunnable
{
    public int timer;

    private final Core core;

    public Start(Core core)
    {
        this.core = core;
        this.timer = core.getConfig().getInt("timers.game");
    }

    public void run()
    {
        if (!core.isState(State.INGAME)) { cancel(); }
        core.timers(timer);
        for (Player pls : core.getServer().getOnlinePlayers()) { pls.setLevel(timer); }
        core.getBossBar().setTitle("§fTemps restant: §b" + timer + "§bs");

        if (core.teams().getRed().size() == core.teams().getRedDeath().size() || core.teams().getRed().size() == 0)
        {
            core.setState(State.FINISH);
            Game game = new Game();
            game.endGame();
            cancel();
        }

        if (core.teams().getBlue().size() == core.teams().getBlueDeath().size() || core.teams().getBlue().size() == 0)
        {
            core.setState(State.FINISH);
            Game game = new Game();
            game.endGame();
            cancel();
        }

        if (timer == 5)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 1.5f);
            }
        }
        if (timer == 4)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 1.2f);
            }
        }
        if (timer == 3)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            }
        }
        if (timer == 2)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 0.7f);
            }
        }
        if (timer == 1)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 0.5f);
            }
        }
        if (timer == 0)
        {
            core.setState(State.FINISH);
            Game game = new Game();
            game.endGame();
            cancel();
        }

        --timer;
    }
}
