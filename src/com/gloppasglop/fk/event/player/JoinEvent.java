package com.gloppasglop.fk.event.player;

import com.gloppasglop.fk.FK;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by christopheroux on 27/08/16.
 */
public class JoinEvent implements Listener {

    private FK plugin;

    public JoinEvent(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getScoreboardHandler().sendPlayerScoreboard(event.getPlayer());
    }
}
