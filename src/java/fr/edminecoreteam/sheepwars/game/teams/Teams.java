package fr.edminecoreteam.sheepwars.game.teams;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Teams
{
    private final List<Player> red;
    private final List<Player> blue;

    private final List<String> redDeath;
    private final List<String> blueDeath;

    private final List<String> redComeBack;
    private final List<String> blueComeBack;
    private static final Core core = Core.getInstance();

    public Teams()
    {
        this.red = new ArrayList<Player>();
        this.blue = new ArrayList<Player>();

        this.redDeath = new ArrayList<String>();
        this.blueDeath = new ArrayList<String>();

        this.redComeBack = new ArrayList<String>();
        this.blueComeBack = new ArrayList<String>();
    }

    public void joinTeam(Player p, String team)
    {
        if (team.equalsIgnoreCase("red") || team.equalsIgnoreCase("blue"))
        {
            if (team.equalsIgnoreCase("red"))
            {
                if (red.contains(p)) { return; }

                if (blue.contains(p)) { blue.remove(p); }

                red.add(p);
                p.sendMessage("§f§lSheepWars §8» §7Vous avez rejoint l'équipe §cRouge§7.");
                return;
            }
            if (team.equalsIgnoreCase("blue"))
            {
                if (blue.contains(p)) { return; }

                if (red.contains(p)) { red.remove(p); }

                blue.add(p);
                p.sendMessage("§f§lSheepWars §8» §7Vous avez rejoint l'équipe §9Bleue§7.");
                return;
            }
        }
        else
        {
            System.out.println("CS PaintBall error, use red or blue.");
            return;
        }
    }

    public void joinRandomTeamButGameIsStart(Player p)
    {
        if (red.contains(p)) { return; }
        if (blue.contains(p)) { return; }

        if (red.size() < core.getConfig().getInt("teams.red.players"))
        {
            red.add(p);
            p.sendMessage("§f§lPaintBall §8» §7Vous avez rejoint l'équipe §cRouge§7.");
            return;
        }
        if (blue.size() < core.getConfig().getInt("teams.blue.players"))
        {
            blue.add(p);
            p.sendMessage("§f§lPaintBall §8» §7Vous avez rejoint l'équipe §9Bleue§7.");
            return;
        }
    }

    public void leaveTeam(Player p)
    {
        if (!red.contains(p) && !blue.contains(p)) { return; }

        if (red.contains(p))
        {
            red.remove(p);
            return;
        }
        if (blue.contains(p))
        {
            blue.remove(p);
            return;
        }
    }

    public List<Player> getTeam(Player p)
    {
        if (!red.contains(p) && !blue.contains(p)) { return null; }

        if (red.contains(p))
        {
            return this.red;
        }
        if (blue.contains(p))
        {
            return this.blue;
        }

        return null;
    }

    public List<Player> getRed()
    {
        return this.red;
    }

    public List<Player> getBlue() { return this.blue; }

    public List<String> getRedDeath()
    {
        return this.redDeath;
    }

    public List<String> getBlueDeath() { return this.blueDeath; }

    public List<String> getRedComeBack()
    {
        return this.redComeBack;
    }

    public List<String> getBlueComeBack() { return this.blueComeBack; }
}
