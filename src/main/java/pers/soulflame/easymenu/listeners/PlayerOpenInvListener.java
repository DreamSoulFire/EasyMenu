package pers.soulflame.easymenu.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.api.MenuAPI;
import pers.soulflame.easymenu.managers.Menu;

import java.util.Map;

public class PlayerOpenInvListener implements Listener {

    @EventHandler
    public void open(InventoryOpenEvent event) {
        final HumanEntity humanEntity = event.getPlayer();
        if (!(humanEntity instanceof Player)) return;
        final Player player = ((Player) humanEntity).getPlayer();
        if (player == null) return;
        final Inventory inventory = event.getInventory();
        final InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof Menu)) return;
        final Menu menu = (Menu) holder;
        Bukkit.getScheduler().runTaskAsynchronously(EasyMenu.getInstance(), () -> {
            Map<Integer, ItemStack> itemMap = MenuAPI.parseToInv(menu.layouts(), menu.icons(), player.getUniqueId());
            inventory.clear();
            itemMap.forEach(inventory::setItem);
        });
    }

}
