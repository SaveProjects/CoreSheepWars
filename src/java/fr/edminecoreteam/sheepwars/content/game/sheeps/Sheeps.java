package fr.edminecoreteam.sheepwars.content.game.sheeps;

import fr.edminecoreteam.sheepwars.content.game.sheeps.sheeps.*;
import fr.edminecoreteam.sheepwars.game.sheeps.sheeps.*;
import org.bukkit.entity.Player;

import java.util.Random;

public class Sheeps
{
    private final Player p;

    public Sheeps(Player p)
    {
        this.p = p;
    }

    public void get()
    {
        SheepsList sheeps = getRandomEnum(SheepsList.class);
        if (sheeps == SheepsList.ABORDAGE)
        {
            ABORDAGE s = new ABORDAGE();
            s.get(p);
        }
        if (sheeps == SheepsList.INCENDIAIRE)
        {
            INCENDIAIRE s = new INCENDIAIRE();
            s.get(p);
        }
        if (sheeps == SheepsList.GLACE)
        {
            GLACE s = new GLACE();
            s.get(p);
        }
        if (sheeps == SheepsList.FOUDRE)
        {
            FOUDRE s = new FOUDRE();
            s.get(p);
        }
        if (sheeps == SheepsList.CHERCHEUR)
        {
            CHERCHEUR s = new CHERCHEUR();
            s.get(p);
        }
        if (sheeps == SheepsList.SOIGNEUR)
        {
            SOIGNEUR s = new SOIGNEUR();
            s.get(p);
        }
        if (sheeps == SheepsList.FRAGMENTATION)
        {
            FRAGMENTATION s = new FRAGMENTATION();
            s.get(p);
        }
        if (sheeps == SheepsList.DISTORSION)
        {
            DISTORSION s = new DISTORSION();
            s.get(p);
        }
        if (sheeps == SheepsList.TREMBLEMENT)
        {
            TREMBLEMENT s = new TREMBLEMENT();
            s.get(p);
        }
        if (sheeps == SheepsList.EXPLOSIF)
        {
            EXPLOSIF s = new EXPLOSIF();
            s.get(p);
        }
        if (sheeps == SheepsList.TENEBREUX)
        {
            TENEBREUX s = new TENEBREUX();
            s.get(p);
        }
        if (sheeps == SheepsList.TSUNAMI)
        {
            TSUNAMI s = new TSUNAMI();
            s.get(p);
        }
        if (sheeps == SheepsList.SINGULARITE)
        {
            SINGULARITE s = new SINGULARITE();
            s.get(p);
        }
        if (sheeps == SheepsList.BONZAI)
        {
            BONZAI s = new BONZAI();
            s.get(p);
        }
    }

    private <T extends Enum<?>> T getRandomEnum(Class<T> clazz) {
        Random random = new Random();
        int randomIndex = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[randomIndex];
    }
}
