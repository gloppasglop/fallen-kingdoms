package com.gloppasglop.fk.event.block;

import com.gloppasglop.fk.FK;
import com.gloppasglop.fk.Team;
import com.gloppasglop.fk.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by christopheroux on 20/08/16.
 */
public class BlockBreak implements Listener{

    private FK plugin;
    public BlockBreak(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();


        if (plugin.getGameState() != FK.GameState.RUNNING) {
            player.sendMessage(ChatColor.DARK_RED + "Game has not yet started.");
            event.setCancelled(true);
            return;
        }

        Location loc = block.getLocation();

        TeamManager tm = plugin.getTeamManager();
        Team teamHere = tm.getTeamByLocation(loc);
        Team teamPlayer = tm.getTeamByPlayer(player);


        if (teamHere == null ) {
            return;
        } else {
            if ( teamHere == teamPlayer ) {
                return;
            } else {
                if ( ! plugin.gametime.isAssault())  {
                    player.sendMessage(ChatColor.DARK_RED+"Assault time not yet started.");
                    event.setCancelled(true);
                } else {
                    //TODO: Handle fallen Kingdom check break
                }
            }
        }

    }
}
