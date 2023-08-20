package pers.soulflame.easymenu.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.soulflame.easymenu.api.MenuAPI;
import pers.soulflame.easymenu.managers.Menu;

import java.util.List;
import java.util.Map;

public class PlayerClickInvListener implements Listener {

    @EventHandler
    public void cantMove(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof Menu)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void runFunction(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof Menu menu)) return;
        int rawSlot = event.getRawSlot();
        Map<Integer, Menu.MenuIcon> parseMap = MenuAPI.parseIconsChar(menu.getLayouts(), menu.getIcons());
        Menu.MenuIcon icon = parseMap.get(rawSlot);
        if (icon == null) return;
        List<Map<?, ?>> functions = icon.functions();
        if (functions == null) return;
    }

}
