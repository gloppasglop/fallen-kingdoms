package com.gloppasglop.fk.utils;

import com.gloppasglop.fk.FK;
import org.bukkit.plugin.Plugin;

/**
 * Created by christopheroux on 27/08/16.
 */
public class GameTime {

    private int time;
    private int pvpDay;
    private int assaultDay;
    private int durationDay;
    private FK plugin;

    public GameTime(FK plugin, int time ) {
        this.time = time;
        this.plugin = plugin;
        this.pvpDay = plugin.getConfigManager().getConfig().getInt("pvpday");
        this.assaultDay = plugin.getConfigManager().getConfig().getInt("assaultday");
        this.durationDay = plugin.getConfigManager().getConfig().getInt("durationday");
        plugin.getLogger().info("durationday" + durationDay);
    }

    public void setTime(int time) {
        this.time = time;
        plugin.getConfigManager().getData().set("time",time);
        plugin.getConfigManager().saveData();;

    }

    public int getTime() {
        return this.time;
    }

    public int days() {
        return 1 + (int) (time/60.0/durationDay);
    }

    public int seconds() {
       return time % 60;
    }

    public int minutes() {
       return (time/60) % durationDay;
    }

    public void inc() {
        time++;
        plugin.getConfigManager().getData().set("time",time);
        plugin.getConfigManager().saveData();;
    }

    public boolean isPvp() {
        return this.days() >= pvpDay;
    }

    public boolean isAssault() {
        return this.days() >= assaultDay;
    }


}
