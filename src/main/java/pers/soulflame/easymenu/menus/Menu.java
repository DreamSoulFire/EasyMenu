package pers.soulflame.easymenu.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.soulflame.easymenu.api.ItemAPI;

import java.util.List;
import java.util.Map;

/**
 * <p>菜单界面构造类</p>
 */
public class Menu implements InventoryHolder {

    private Inventory inventory;

    private final String title;
    private final Integer size;
    private final String layouts;
    private final Map<String, MenuIcon> icons;

    public Menu(String title, Integer size, String layouts, Map<String, MenuIcon> icons) {
        this.title = title;
        this.size = size;
        this.layouts = layouts;
        this.icons = icons;
        create();
    }

    public record MenuIcon(String source, Map<String, ?> item, List<Map<?, ?>> functions) {}

    private void create() {
        inventory = Bukkit.createInventory(this, size, title);
        final Map<Integer, ItemStack> items = ItemAPI.parseInventory(getLayouts(), getIcons());
        items.forEach((slot, item) -> getInventory().setItem(slot, item));
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    /**
     * <p>获取菜单标题</p>
     *
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>获取菜单大小</p>
     *
     * @return 大小
     */
    public Integer getSize() {
        return size;
    }

    /**
     * <p>获取菜单样式</p>
     *
     * @return 样式集合
     */
    public String getLayouts() {
        return layouts;
    }

    /**
     * <p>获取菜单内所有物品图标</p>
     *
     * @return 图标的map
     */
    public Map<String, MenuIcon> getIcons() {
        return icons;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
