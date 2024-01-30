package fr.edminecoreteam.sheepwars;

import fr.edminecoreteam.sheepwars.game.GameListeners;
import fr.edminecoreteam.sheepwars.game.teams.Teams;
import fr.edminecoreteam.sheepwars.game.teams.displayname.ChatTeam;
import fr.edminecoreteam.sheepwars.game.teams.displayname.TabListTeams;
import fr.edminecoreteam.sheepwars.listeners.connection.JoinEvent;
import fr.edminecoreteam.sheepwars.listeners.connection.LeaveEvent;
import fr.edminecoreteam.sheepwars.utils.game.SpawnListeners;
import fr.edminecoreteam.sheepwars.utils.minecraft.BossBar;
import fr.edminecoreteam.sheepwars.utils.minecraft.TitleBuilder;
import fr.edminecoreteam.sheepwars.utils.scoreboards.JoinScoreboardEvent;
import fr.edminecoreteam.sheepwars.utils.scoreboards.LeaveScoreboardEvent;
import fr.edminecoreteam.sheepwars.utils.scoreboards.ScoreboardManager;
import fr.edminecoreteam.sheepwars.utils.scoreboards.WorldChangeScoreboardEvent;
import fr.edminecoreteam.sheepwars.utils.world.LoadWorld;
import fr.edminecoreteam.sheepwars.waiting.WaitingListeners;
import fr.edminecoreteam.sheepwars.waiting.guis.ChooseTeam;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Core extends JavaPlugin
{
    private static Core instance;
    private State state;
    private static Plugin plugin;
    public MySQL database;
    public String world;
    public int timers;
    public void timers(int i) { this.timers = i; }
    private int maxplayers;
    private List<String> playersInGame;
    public boolean isForceStart = false;
    private Teams teams;
    private SpawnListeners spawnListeners;
    private BossBar bossBar;
    private TitleBuilder title;
    private ScoreboardManager scoreboardManager;
    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadGameWorld();
        loadListeners();
        ScoreboardManager();

        //MySQLConnect();

        setState(State.WAITING);
    }

    private void MySQLConnect()
    {
        (this.database = new MySQL(
                "jdbc:mysql://",
                this.getConfig().getString("mysql.host"),
                this.getConfig().getString("mysql.database"),
                this.getConfig().getString("mysql.user"),
                this.getConfig().getString("mysql.password")
        )).connexion();
    }

    private void loadListeners()
    {
        this.playersInGame = new ArrayList<String>();
        this.teams = new Teams();
        this.spawnListeners = new SpawnListeners();
        this.title = new TitleBuilder();
        this.bossBar = new BossBar(this, "SheepWars");
        maxplayers = getConfig().getInt("teams.red.players") + getConfig().getInt("teams.blue.players");
        Bukkit.getPluginManager().registerEvents((Listener) new JoinEvent(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener) new LeaveEvent(), (Plugin)this);

        Bukkit.getPluginManager().registerEvents((Listener) new WaitingListeners(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener) new ChooseTeam(), (Plugin)this);

        Bukkit.getPluginManager().registerEvents((Listener) new GameListeners(), (Plugin)this);

        Bukkit.getPluginManager().registerEvents((Listener) new TabListTeams(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener) new ChatTeam(), (Plugin)this);
    }

    private void loadGameWorld()
    {
        String world = LoadWorld.getRandomSubfolderName("gameTemplate/");
        LoadWorld.createGameWorld(world);
        this.world = world;
    }

    private void ScoreboardManager()
    {
        Bukkit.getPluginManager().registerEvents(new JoinScoreboardEvent(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveScoreboardEvent(), this);
        Bukkit.getPluginManager().registerEvents(new WorldChangeScoreboardEvent(), this);

        scheduledExecutorService = Executors.newScheduledThreadPool(16);
        executorMonoThread = Executors.newScheduledThreadPool(1);
        scoreboardManager = new ScoreboardManager();
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }
    public ScheduledExecutorService getExecutorMonoThread() {
        return this.executorMonoThread;
    }
    public ScheduledExecutorService getScheduledExecutorService() {
        return this.scheduledExecutorService;
    }
    public Teams teams() { return this.teams; }
    public SpawnListeners spawnListeners() { return this.spawnListeners; }
    public void setState(State state) { this.state = state; }
    public boolean isState(State state) { return this.state == state; }
    public int getMaxplayers() { return this.maxplayers; }
    public List<String> getPlayersInGame() { return this.playersInGame; }
    public BossBar getBossBar() { return this.bossBar; }
    public TitleBuilder titleBuilder() { return this.title; }
    public static Core getInstance() { return Core.instance; }
    public static Plugin getPlugin() { return Core.plugin; }
}
