package pers.soulflame.easymenu.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.api.MenuAPI;
import pers.soulflame.easymenu.managers.Menu;

public class PlayerClickInvListener implements Listener {

    @EventHandler
    public void cantMove(InventoryClickEvent event) {
        final var inventory = event.getClickedInventory();
        if (inventory == null) return;
        final var holder = inventory.getHolder();
        if (!(holder instanceof Menu)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void runFunction(InventoryClickEvent event) {
        final var whoClicked = event.getWhoClicked();
        if (!(whoClicked instanceof final Player player)) return;
        final var inventory = event.getClickedInventory();
        if (inventory == null) return;
        final var holder = inventory.getHolder();
        if (!(holder instanceof final Menu menu)) return;
        final var rawSlot = event.getRawSlot();
        var resultMap = MenuAPI.parseInv(menu.layouts(), menu.icons(), player.getUniqueId());
        final var icon = MenuAPI.getMenuIcon(resultMap, rawSlot);
        if (icon == null) return;
        final var click = event.getClick();
        Bukkit.getScheduler().runTaskAsynchronously(EasyMenu.getInstance(),
                () -> icon.runFunctions(player.getUniqueId(), click));
    }

}
