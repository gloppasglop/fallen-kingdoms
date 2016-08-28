package com.gloppasglop.fk.event.player;

import com.gloppasglop.fk.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by christopheroux on 27/08/16.
 */
public class JoinEvent implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.getScoreboardHandler().sendPlayerScoreboard(event.getPlayer());
    }
}
