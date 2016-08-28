package com.gloppasglop.fk.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by christopheroux on 20/08/16.
 */
public class ChatEvent implements Listener{

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        event.setCancelled(true);
        if ( message.startsWith("!")) {

        }

    }
}
