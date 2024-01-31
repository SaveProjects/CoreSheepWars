package fr.edminecoreteam.sheepwars.game;

import fr.edminecoreteam.sheepwars.Core;
import fr.edminecoreteam.sheepwars.end.EndListeners;
import fr.edminecoreteam.sheepwars.game.kits.DefaultKit;
import fr.edminecoreteam.sheepwars.game.sheeps.tasks.GiveSheep;
import fr.edminecoreteam.sheepwars.game.tasks.Preparation;
import fr.edminecoreteam.sheepwars.game.tasks.Start;
import fr.edminecoreteam.sheepwars.utils.game.GameUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Game
{
    private static final Core core = Core.getInstance();
    private final GameUtils gameUtils = new GameUtils();

    public void preparation()
    {
        for (Player red : core.teams().getRed())
        {
            red.getInventory().clear();
            gameUtils.teleportPlayer(red, gameUtils.getRedSpawn());
            red.sendTitle("§d§lPréparez vous !", "§7Ça va chauffer !");
            DefaultKit defaultKit = new DefaultKit(red);
            defaultKit.equip();
        }
        for (Player blue : core.teams().getBlue())
        {
            blue.getInventory().clear();
            gameUtils.teleportPlayer(blue, gameUtils.getBlueSpawn());
            blue.sendTitle("§d§lPréparez vous !", "§7Ça va chauffer !");
            DefaultKit defaultKit = new DefaultKit(blue);
            defaultKit.equip();
        }
        Preparation task = new Preparation(core);
        task.runTaskTimer((Plugin) core, 0L, 20L);
    }

    public void startGame()
    {
        for (Player red : core.teams().getRed())
        {
            red.sendTitle("§d§lC'est la guerre !", "§7Bonne chance !");
            DefaultKit defaultKit = new DefaultKit(red);
            defaultKit.armed();
        }
        for (Player blue : core.teams().getBlue())
        {
            blue.sendTitle("§d§lC'est la guerre !", "§7Bonne chance !");
            DefaultKit defaultKit = new DefaultKit(blue);
            defaultKit.armed();
        }
        Start task = new Start(core);
        task.runTaskTimer((Plugin) core, 0L, 20L);

        GiveSheep giveSheep = new GiveSheep(core);
        giveSheep.runTaskTimer((Plugin) core, 0L, 20L);
    }

    public void endGame()
    {
        EndListeners endListeners = new EndListeners();
        endListeners.end();
    }
}
