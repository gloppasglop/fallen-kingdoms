package com.gloppasglop.fk.event.block;

import com.gloppasglop.fk.FK;
import com.gloppasglop.fk.Team;
import com.gloppasglop.fk.TeamManager;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by christopheroux on 29/08/16.
 */
public class BlockPlace implements Listener{

    private FK plugin;

    public BlockPlace(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlacedEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();

        // TODO : Check game status

        if (material == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            if ( chest.getBlockInventory().getTitle().equals("§aFallen Kingdom Chest")) {

                Location loc = block.getLocation();

                TeamManager tm = plugin.getTeamManager();
                Team teamHere = tm.getTeamByLocation(loc);
                Team teamPlayer = tm.getTeamByPlayer(player);

                if (teamPlayer == null) {
                    player.sendMessage(ChatColor.DARK_RED + "You must be part of a team to place this chest.");
                    event.setCancelled(true);
                } else if (teamHere == null) {
                    player.sendMessage(ChatColor.DARK_RED + "This chest can only be placed in a base.");
                    event.setCancelled(true);
                } else if (teamHere != teamPlayer) {
                    player.sendMessage(ChatColor.DARK_RED + "This chest can only be placed in your base.");
                    event.setCancelled(true);
                } else if (teamPlayer.getChestLocation() != null) {
                    player.sendMessage(ChatColor.DARK_RED + "You can only have one chest in yout base.");
                    event.setCancelled(true);
                } else {

                    BukkitTask effectTask = new BukkitRunnable() {
                        double phi = 0;

                        @Override
                        public void run() {
                            phi += Math.PI / 10;
                            for (double theta = 0; theta < 2 * Math.PI; theta += Math.PI / 40) {
                                double r = 1.5;
                                double x = r * Math.cos(theta) * Math.sin(phi) + 0.5;
                                double y = r * Math.cos(phi);
                                double z = r * Math.sin(theta) * Math.sin(phi) + 0.5;

                                loc.add(x, y, z);
                                loc.getWorld().spigot().playEffect(loc, Effect.FLAME, 34, 0, 0.0f, 0.0f, 0.0f, 0.0f, 1, 10);
                                loc.subtract(x, y, z);
                            }
                        }
                    }.runTaskTimer(plugin, 0, 2);

                    teamPlayer.setChestLocation(loc);
                    if (teamPlayer.getEffectTask() != null) {
                        teamPlayer.getEffectTask().cancel();
                    }

                    teamPlayer.setEffectTask(effectTask);

                }
            }
        }
    }
}
