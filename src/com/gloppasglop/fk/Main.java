package com.gloppasglop.fk;

import com.gloppasglop.fk.event.block.BlockBreak;
import com.gloppasglop.fk.event.block.BlockDamage;
import com.gloppasglop.fk.event.player.ChatEvent;
import com.gloppasglop.fk.event.player.PlayerDamage;
import com.gloppasglop.fk.utils.GameTime;
import com.gloppasglop.fk.utils.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by christopheroux on 20/08/16.
 */
public class Main extends JavaPlugin{


    private static ScoreboardHandler scoreboardHandler;

    public static GameTime gametime;

    private static Plugin plugin;

    private static List<Team> teams = new ArrayList<>();

    public static ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    public static void setScoreboardHandler(ScoreboardHandler scoreboardHandler) {
        Main.scoreboardHandler = scoreboardHandler;
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static void setTeams(List<Team> teams) {
        Main.teams = teams;
    }

    public static Team getTeam(String name) {
        for (Team team : getTeams()) {
            if (team.getTeam().getName().equalsIgnoreCase(name) ) {
                return team;
            }
        }
        return null;
    }

    public static Plugin getInstance() {
        return plugin;
    }

    public void onEnable() {

        plugin = this;

        getLogger().info(getDescription().getName() + " has been enabled!");
        getCommand("fk").setExecutor(new com.gloppasglop.fk.commands.Fk());

        gametime = new GameTime();

        setScoreboardHandler(new ScoreboardHandler());

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                getScoreboardHandler().updateTime();
                Iterator players = Bukkit.getOnlinePlayers().iterator();

                while(players.hasNext()) {
                    Player pls = (Player) players.next();
                    getScoreboardHandler().sendPlayerScoreboard(pls);
                }
                boolean prevIsPvp = gametime.isPvp();
                boolean prevIsAssault = gametime.isAssault();
                gametime.inc();
                if ( gametime.isPvp() && ! prevIsPvp ) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "PVP mode started. Let's fight!!!");
                }
                if (gametime.isAssault() && ! prevIsAssault) {
                    Bukkit.broadcastMessage((ChatColor.YELLOW + "Assault mode started!"));
                }
            }
        }, 0, 20);


        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aFallen Kingdom Chest");
        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(item);
        recipe.shape("DDD","DCD","DDD");
        recipe.setIngredient('D',Material.DIAMOND);
        recipe.setIngredient('C',Material.CHEST);

        Bukkit.addRecipe(recipe);

        BossBar bossbar = Bukkit.createBossBar("Essai", BarColor.BLUE, BarStyle.SEGMENTED_12, BarFlag.CREATE_FOG);
        bossbar.setProgress(1.0);


        registerEvents();
        registerConfig();

        initTeams();

    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new BlockBreak(), this);
        pm.registerEvents(new ChatEvent(), this);
        pm.registerEvents(new BlockDamage(), this);
        pm.registerEvents(new PlayerDamage(), this);
    }

    private void registerConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void initTeams() {
        Scoreboard sc =getScoreboardHandler().getScoreboard();
        String[] teamColours = {"RED","BLUE","GREEN","YELLOW"};
        Location loc = new Location(getServer().getWorld("world"),0.0D, 0.0D, 0.0D);
        for ( String colour: teamColours) {
            org.bukkit.scoreboard.Team teamBukkit=sc.registerNewTeam(colour);
            Team team=new Team(loc,teamBukkit);
            team.disable();
            teams.add(team);
        }
    }
}
