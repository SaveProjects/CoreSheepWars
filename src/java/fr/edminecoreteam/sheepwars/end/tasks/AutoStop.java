package fr.edminecoreteam.sheepwars.end.tasks;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoStop extends BukkitRunnable
{
    public int timer;

    private final Core core;

    public AutoStop(Core core)
    {
        this.core = core;
        this.timer = 30;
    }

    public void run()
    {
        core.timers(timer);

        if (timer == 29) {
            for (Player pls : Bukkit.getOnlinePlayers())
            {
                pls.playSound(pls.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 1.0f);
            }
        }
        if (timer == 28) {
            for (Player pls : Bukkit.getOnlinePlayers())
            {
                pls.playSound(pls.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 1.0f);
            }
        }
        if (timer == 27) {
            for (Player pls : Bukkit.getOnlinePlayers())
            {
                pls.playSound(pls.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 1.0f);
            }
        }
        if (timer == 26) {
            for (Player pls : Bukkit.getOnlinePlayers())
            {
                pls.playSound(pls.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 1.0f);
            }
        }
        if (timer == 25) {
            for (Player pls : Bukkit.getOnlinePlayers())
            {
                pls.playSound(pls.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 1.0f);
            }
        }
        if (timer == 24) {
            for (Player pls : Bukkit.getOnlinePlayers())
            {
                pls.playSound(pls.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
            }
        }


        if (timer == 10)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.CLICK, 1.0f, 1.5f);
            }
            core.getServer().broadcastMessage("§6Arrêt du serveur dans 10 secondes.");
        }
        if (timer == 5)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.CLICK, 1.0f, 1.5f);
            }
        }
        if (timer == 4)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.CLICK, 1.0f, 1.2f);
            }
        }
        if (timer == 3)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.CLICK, 1.0f, 1.0f);
            }
        }
        if (timer == 2)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.CLICK, 1.0f, 0.7f);
            }
        }
        if (timer == 1)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.CLICK, 1.0f, 0.5f);
            }
            core.getServer().broadcastMessage("§cArrêt du serveur...");
        }
        if (timer == 0)
        {
            for (Player pls : core.getServer().getOnlinePlayers()) {
                pls.kickPlayer("");
            }
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "stop");
            cancel();
        }
        --timer;
    }
}
