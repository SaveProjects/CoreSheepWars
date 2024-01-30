package fr.edminecoreteam.sheepwars.waiting.tasks;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.State;
import fr.edminecoreteam.sheepwars.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoStart extends BukkitRunnable
{
    public int timer;

    private Core core;

    public AutoStart(Core core)
    {
        this.core = core;
        this.timer = core.getConfig().getInt("timers.start");
    }

    public void run()
    {
        core.timers(timer);

        if (core.getConfig().getString("type").equalsIgnoreCase("ranked"))
        {
            if (core.getPlayersInGame().size() < core.getMaxplayers() && !core.isForceStart)
            {
                for (Player pls : core.getServer().getOnlinePlayers()) {
                    pls.setLevel(0);
                    pls.playSound(pls.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    pls.sendTitle("", "");
                }
                Bukkit.broadcastMessage("§cErreur de lancement, il manque des joueurs...");
                core.setState(State.WAITING);
                cancel();
            }
            else if (core.getPlayersInGame().size() == core.getMaxplayers())
            {
                if (timer > core.getConfig().getInt("timers.startfull"))
                {
                    timer = core.getConfig().getInt("timers.startfull");
                }
            }
        }
        else if (core.getConfig().getString("type").equalsIgnoreCase("unranked"))
        {
            if (core.getPlayersInGame().size() < core.getConfig().getInt("needtostart") && !core.isForceStart)
            {
                for (Player pls : core.getServer().getOnlinePlayers()) {
                    pls.setLevel(0);
                    pls.playSound(pls.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                    pls.sendTitle("", "");
                }
                Bukkit.broadcastMessage("§cErreur de lancement, il manque des joueurs...");
                core.setState(State.WAITING);
                cancel();
            }
            else if (core.getPlayersInGame().size() == core.getMaxplayers())
            {
                if (timer > core.getConfig().getInt("timers.startfull"))
                {
                    timer = core.getConfig().getInt("timers.startfull");
                }
            }
        }

        for (Player pls : core.getServer().getOnlinePlayers()) {
            pls.setLevel(timer);
            if (timer != 5 && timer != 4 && timer != 3 && timer != 2 && timer != 1) {
                pls.playSound(pls.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
            }
        }
        if (timer == 20)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§e§lLancement du jeu", "§7dans §7" + timer + " §7secondes...");
            }
        }
        if (timer == 10)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§e§lLancement du jeu", "§7dans §7" + timer + " §7secondes...");
            }
        }
        if (timer == 5)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§a§l" + timer, "§7préparez-vous.");
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 1.5f);
            }
        }
        if (timer == 4)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§6§l" + timer, "");
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 1.2f);
            }
        }
        if (timer == 3)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§e§l" + timer, "");
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            }
        }
        if (timer == 2)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§c§l" + timer, "");
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 0.7f);
            }
        }
        if (timer == 1)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.sendTitle("§4§l" + timer, "");
                pls.playSound(pls.getLocation(), Sound.NOTE_PLING, 1.0f, 0.5f);
            }
        }
        if (timer == 0)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                System.out.println(pls);
                core.teams().joinRandomTeamButGameIsStart(pls);
            }
            core.setState(State.PREPARATION);
            Game game = new Game();
            game.preparation();
            cancel();
        }
        --timer;
    }
}
