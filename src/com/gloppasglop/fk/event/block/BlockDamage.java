package com.gloppasglop.fk.event.block;

import com.gloppasglop.fk.FK;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

/**
 * Created by christopheroux on 28/08/16.
 */
public class BlockDamage implements Listener {

    private FK plugin;

    public BlockDamage(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockDamagedEvent(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();

        if ( material == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            player.sendMessage(ChatColor.RED + chest.getBlockInventory().getTitle());
            event.setCancelled(true);
        }
    }
}
