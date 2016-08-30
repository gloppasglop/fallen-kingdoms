package com.gloppasglop.fk.event.player;

import com.gloppasglop.fk.FK;
import com.gloppasglop.fk.Team;
import com.gloppasglop.fk.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by christopheroux on 30/08/16.
 */
public class EnterZoneEvent implements Listener {

    private FK plugin;

    private Map<String, String>  previousTeamBase = new HashMap<>();


    public EnterZoneEvent(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Team team;
        TeamManager tm = plugin.getTeamManager();
        String playername = player.getName();

        if ( !previousTeamBase.containsKey(playername)) {
            previousTeamBase.put(playername,null);
        }


        Team currentBase = tm.getTeamByLocation(event.getTo());
        String currentBaseName;
        if ( currentBase == null) {
           currentBaseName = null;
        } else {
            currentBaseName = currentBase.getName();
        }

        if ( currentBaseName != null ) {

            String previousBaseName = previousTeamBase.get(playername);

            if (previousBaseName == null || !previousBaseName.equals(currentBaseName)) {
                previousTeamBase.put(playername,currentBaseName);
                player.sendMessage(ChatColor.YELLOW+"Entering base of team "+ currentBaseName);

            }
        } else {
            String previousBaseName = previousTeamBase.get(playername);
            if (previousBaseName != null) {
                previousTeamBase.put(playername,null);
                player.sendMessage(ChatColor.YELLOW+"Leaving base of team "+ previousBaseName);
            }
        }
    }
}
