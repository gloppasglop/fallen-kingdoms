package com.gloppasglop.fk.event.player;

import com.gloppasglop.fk.FK;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Created by christopheroux on 01/09/16.
 */
public class PlayerFood implements Listener {

    private FK plugin;

    public PlayerFood(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent event) {

        if (plugin.getGameState() != FK.GameState.RUNNING) {
            event.setCancelled(true);
        }
    }
}
