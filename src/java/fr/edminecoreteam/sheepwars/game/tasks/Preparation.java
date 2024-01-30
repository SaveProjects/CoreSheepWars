package fr.edminecoreteam.sheepwars.game.tasks;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.game.Game;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Preparation extends BukkitRunnable
{
    public int timer;

    private final Core core;

    public Preparation(Core core)
    {
        this.core = core;
        this.timer = core.getConfig().getInt("timers.preparation");
    }

    public void run()
    {
        if (!core.isState(State.PREPARATION)) { cancel(); }
        core.timers(timer);
        for (Player pls : core.getServer().getOnlinePlayers()) { pls.setLevel(timer); }
        core.getBossBar().setTitle("§fPréparation: §e" + timer + "§es");
        for (Player pls : core.getServer().getOnlinePlayers()) {
            if (timer <= 15 && timer != 5 && timer != 4 && timer != 3 && timer != 2 && timer != 1) {
                pls.playSound(pls.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
            }
        }
        if (timer == 5)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§a§l" + timer, "§7préparez-vous.");
                pls.playSound(pls.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.5f);
            }
        }
        if (timer == 4)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§6§l" + timer, "");
                pls.playSound(pls.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.2f);
            }
        }
        if (timer == 3)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§e§l" + timer, "");
                pls.playSound(pls.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
            }
        }
        if (timer == 2)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§c§l" + timer, "");
                pls.playSound(pls.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.7f);
            }
        }
        if (timer == 1)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§4§l" + timer, "");
                pls.playSound(pls.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
            }
        }
        if (timer == 0)
        {
            core.setState(State.INGAME);
            Game game = new Game();
            game.startGame();
            cancel();
        }

        --timer;
    }
}
