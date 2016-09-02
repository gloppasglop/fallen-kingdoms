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


        if (event.getDamager() instanceof Player) {

            Player damager = (Player) event.getDamager();
            if (plugin.getGameState() != FK.GameState.RUNNING) {
                event.setCancelled(true);
                damager.sendMessage(ChatColor.DARK_RED+ "Game has not started or is paused!");
            } else {
                if (!(plugin.gametime.isPvp() || plugin.gametime.isAssault()) && event.getEntity() instanceof Player) {
                    damager.sendMessage(ChatColor.DARK_RED + "PVP has not yet started!");
                }
            }
        } else {
            if (plugin.getGameState() != FK.GameState.RUNNING) {
                event.setCancelled(true);
            }
        }

    }

}

