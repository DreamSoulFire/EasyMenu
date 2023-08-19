package pers.soulflame.easymenu.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.soulflame.easymenu.menus.Menu;

public class PlayerClickInvListener implements Listener {

    @EventHandler
    public void cantMove(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof Menu)) return;
        event.setCancelled(true);
    }

}
