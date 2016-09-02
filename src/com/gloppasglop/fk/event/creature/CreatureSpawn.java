package com.gloppasglop.fk.event.creature;

import com.gloppasglop.fk.FK;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Created by christopheroux on 01/09/16.
 */
public class CreatureSpawn implements Listener {

    private FK plugin;

    public CreatureSpawn(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        if (plugin.getGameState() != FK.GameState.RUNNING) {
            event.setCancelled(true);
        }

    }
}
