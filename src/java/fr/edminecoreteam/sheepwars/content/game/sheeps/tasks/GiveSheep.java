package fr.edminecoreteam.sheepwars.content.game.sheeps.tasks;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.content.game.sheeps.Sheeps;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GiveSheep extends BukkitRunnable
{
    private final Core core;
    private int timer;

    public GiveSheep(Core core)
    {
        this.core = core;
        this.timer = 15;
    }
    public void run()
    {
        if (!core.isState(State.INGAME)) { cancel(); }

        if (timer == 0)
        {
            timer = 15;
            for (Player pls : core.teams().getRed())
            {
                Sheeps sheeps = new Sheeps(pls);
                sheeps.get();
            }
            for (Player pls : core.teams().getBlue())
            {
                Sheeps sheeps = new Sheeps(pls);
                sheeps.get();
            }
        }

        --timer;
    }
}
