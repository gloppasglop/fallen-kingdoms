package com.gloppasglop.fk.event.player;

import com.gloppasglop.fk.FK;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by christopheroux on 28/08/16.
 */
public class PlayerDamage implements Listener {

    private FK plugin;

    public PlayerDamage(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof  Player) {
            Player damager = (Player) event.getDamager();
            Player damagee = (Player) event.getEntity();
            if ( plugin.getGameState() != FK.GameState.RUNNING) {
                damager.sendMessage(ChatColor.DARK_RED+ "Game has not started or is paused!");
                event.setCancelled(true);
            } else {
                if ( ! (plugin.gametime.isPvp() || plugin.gametime.isAssault())) {
                    damager.sendMessage(ChatColor.DARK_RED + "PVP has not yet started!");
                    event.setCancelled(true);
                }
            }
        }
    }
}

