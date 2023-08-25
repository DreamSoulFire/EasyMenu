package pers.soulflame.easymenu.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * <p>菜单界面构造类</p>
 */
public record Menu(String title, Integer size, Map<String, Object> condition, String layouts, Map<String, Object> icons) implements InventoryHolder {

    private static Inventory inventory;

    /**
     * <p>为玩家打开一个菜单界面</p>
     *
     * @param player 需打开界面的玩家
     */
    public void open(Player player) {
        inventory = Bukkit.createInventory(this, size, title);
        player.closeInventory();
        player.openInventory(getInventory());
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
