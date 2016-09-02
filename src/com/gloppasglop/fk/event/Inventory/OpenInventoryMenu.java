package com.gloppasglop.fk.event.Inventory;

import com.gloppasglop.fk.FK;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by christopheroux on 02/09/16.
 */
public class OpenInventoryMenu implements Listener {

    private FK plugin;

    public OpenInventoryMenu(FK plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public  void  onClickInventory(InventoryClickEvent event) {

        if (! event.getClickedInventory().equals(plugin.getTeamGui())) {
            return;
        }

        ItemStack i = event.getCurrentItem();
        ItemMeta im = i.getItemMeta();
        if (ChatColor.stripColor(im.getDisplayName()).equals("Team")) {

            String teamname = ChatColor.stripColor(im.getLore().get(0));
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("You have chosen team: "+teamname);
            event.getWhoClicked().closeInventory();
        }
    }
}
