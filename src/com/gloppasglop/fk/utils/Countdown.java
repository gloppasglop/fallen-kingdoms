package com.gloppasglop.fk.utils;

import com.gloppasglop.fk.FK;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by christopheroux on 28/08/16.
 */
public class Countdown extends BukkitRunnable {

    private final FK plugin;
    private int counter;

    public Countdown(FK plugin, int counter) {
        this.plugin = plugin;
        if (counter < 1) {
            throw new IllegalArgumentException("Counter must be greater than 1!");
        } else {
            this.counter = counter;
        }

    }

    @Override
    public void run() {
        if (counter > 0) {
            plugin.getServer().broadcastMessage(ChatColor.GOLD + "Game start in " + counter--);
        } else {
            plugin.getServer().broadcastMessage(ChatColor.GOLD + "Starting game. Have fun!");
            plugin.gametime.start();
            this.cancel();
        }

    }
}
