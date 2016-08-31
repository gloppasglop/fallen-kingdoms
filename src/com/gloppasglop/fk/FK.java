package com.gloppasglop.fk;

import com.gloppasglop.fk.event.block.BlockBreak;
import com.gloppasglop.fk.event.block.BlockDamage;
import com.gloppasglop.fk.event.block.BlockPlace;
import com.gloppasglop.fk.event.player.ChatEvent;
import com.gloppasglop.fk.event.player.EnterZoneEvent;
import com.gloppasglop.fk.event.player.JoinEvent;
import com.gloppasglop.fk.event.player.PlayerDamage;
import com.gloppasglop.fk.utils.ConfigManager;
import com.gloppasglop.fk.utils.GameTime;
import com.gloppasglop.fk.utils.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by christopheroux on 20/08/16.
 */
public class FK extends JavaPlugin{


    public enum GameState {LOBBY,STARTING, RUNNING, PAUSED, STOPPED};
    private TeamManager teamManager = new TeamManager(this);
    private ConfigManager configManager = new ConfigManager(this);
    private ScoreboardHandler scoreboardHandler;

    private GameState gameState;

    public void setGameState(GameState state) {
        this.gameState = state;
        configManager.getData().set("state",state.toString());
    }

    public void setGameState(String state) {
        //TODO: ctch invalid values
        this.setGameState(GameState.valueOf(state));
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameTime gametime;

    public ConfigManager getConfigManager() {
        return configManager;
    }

    private List<Team> teams = new ArrayList<>();

    private File configfile, gameconfigfile;
    private FileConfiguration config, gameconfig;

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    private void setScoreboardHandler(ScoreboardHandler scoreboardHandler) {
        this.scoreboardHandler = scoreboardHandler;
    }

//    public List<Team> getTeams() {
 //       return teams;
//    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

//    public Team getTeam(String name) {
//        for (Team team : getTeams()) {
//            if (team.getTeam().getName().equalsIgnoreCase(name) ) {
//                return team;
//            }
//        }
//        return null;
 //   }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public void onEnable() {

        getLogger().info(getDescription().getName() + " has been enabled!");
        registerConfig();
        getCommand("fk").setExecutor(new com.gloppasglop.fk.commands.Fk(this));

        gametime = new GameTime(this,0);

        String gameStateConfig = configManager.getData().getString("state");

        if (gameStateConfig == null) {
            configManager.getData().set("state","LOBBY");
            configManager.getData().set("time",0);
            configManager.saveData();
            setGameState(GameState.LOBBY);
            gametime.setTime(0);
        } else {
            setGameState(gameStateConfig);
            Integer timeConfig = configManager.getData().getInt("time");
            if ( timeConfig == null) {
                configManager.getData().set("time",0);
                configManager.saveData();
                gametime.setTime(0);
            } else {
                gametime.setTime(timeConfig);
            }
        }

        teamManager.loadConfig();


        setScoreboardHandler(new ScoreboardHandler(this));

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            getScoreboardHandler().updateTime();

            for (Player pls : Bukkit.getOnlinePlayers()) {
                getScoreboardHandler().sendPlayerScoreboard(pls);
            }
            boolean prevIsPvp = gametime.isPvp();
            boolean prevIsAssault = gametime.isAssault();
            if (gameState == GameState.RUNNING) {
                gametime.inc();
                configManager.getData().set("time",gametime.getTime());
                configManager.saveData();
            }

            if ( gametime.isPvp() && ! prevIsPvp ) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "PVP mode started. Let's fight!!!");
            }
            if (gametime.isAssault() && ! prevIsAssault) {
                Bukkit.broadcastMessage((ChatColor.YELLOW + "Assault mode started!"));
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


        registerListeners(new JoinEvent(this),
                          new BlockBreak(this),
                          new BlockDamage(this),
                          new BlockPlace(this),
                          new ChatEvent(this),
                          new EnterZoneEvent(this),
                          new PlayerDamage(this));




        //initTeams();

    }

    public boolean isInBase(Player player, Team team) {
        Double plX = player.getLocation().getX();
        Double plZ = player.getLocation().getZ();

        if ( team.getCenter() == null) {
            return false;
        }

        // Make sure in same world
        if (player.getWorld() != team.getCenter().getWorld()) {
            return false;
        }

        Double baseX = team.getCenter().getX();
        Double baseZ = team.getCenter().getZ();

        Double baseSize= team.getSize().doubleValue();

        if ( ( Math.abs(baseX-plX) <= baseSize/2 ) && ( Math.abs(baseZ-plZ) <= baseSize/2 )) {
            return true;
        } else {
            return false;
        }

    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

    private void registerListeners(Listener... listeners) {

        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener,this));
    }

    private void registerConfig() {
        configManager.setup();
    }


}
