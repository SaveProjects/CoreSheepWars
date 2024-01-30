package fr.edminecoreteam.sheepwars;

public enum State
{
    WAITING("WAITING", 0),
    STARTING("STARTING", 1),
    PREPARATION("PREPARATION", 2),
    INGAME("INGAME", 3),
    FINISH("FINISH", 4);

    private State(final String name, final int ordinal) {
    }
}
