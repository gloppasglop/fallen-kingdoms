package com.gloppasglop.fk.event.player;

import com.gloppasglop.fk.FK;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by christopheroux on 02/09/16.
 */
public class PlayerFall implements Listener {

    private FK plugin;

    public PlayerFall(FK plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {

        if (event.getEntity() instanceof  Player) {
            Player player = (Player) event.getEntity();

            if (plugin.getGameState() == FK.GameState.STARTING) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    player.setFallDistance(0);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
