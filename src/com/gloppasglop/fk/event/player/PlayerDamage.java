package com.gloppasglop.fk.event.player;

import com.gloppasglop.fk.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by christopheroux on 28/08/16.
 */
public class PlayerDamage implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof  Player) {
            Player damager = (Player) event.getDamager();
            Player damagee = (Player) event.getEntity();
            if (! Main.gametime.isRunning()) {
                damager.sendMessage(ChatColor.DARK_RED+ "Game has not started or is paused!");
                event.setCancelled(true);
            } else if (Main.gametime.isPvp() || Main.gametime.isAssault()) {
            } else {
               damager.sendMessage(ChatColor.DARK_RED+ "PVP has not yet started!");
               event.setCancelled(true);
            }
        }
    }
}

