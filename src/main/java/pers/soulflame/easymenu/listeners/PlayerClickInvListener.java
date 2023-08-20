package pers.soulflame.easymenu.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pers.soulflame.easymenu.api.MenuAPI;
import pers.soulflame.easymenu.managers.Menu;
import pers.soulflame.easymenu.managers.MenuIcon;

import java.util.Map;

public class PlayerClickInvListener implements Listener {

    @EventHandler
    public void cantMove(InventoryClickEvent event) {
        final Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof Menu)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void runFunction(InventoryClickEvent event) {
        final HumanEntity whoClicked = event.getWhoClicked();
        if (!(whoClicked instanceof final Player player)) return;
        final Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        final InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof final Menu menu)) return;
        final int rawSlot = event.getRawSlot();
        final Map<Integer, MenuIcon> parseMap = MenuAPI.parseIconsChar(menu.layouts(), menu.icons());
        final MenuIcon icon = parseMap.get(rawSlot);
        if (icon == null) return;
        icon.runFunctions(player.getUniqueId());
    }

}
