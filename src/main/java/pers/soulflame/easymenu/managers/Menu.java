package pers.soulflame.easymenu.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.soulflame.easymenu.api.MenuAPI;

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
    private Map<Integer, ItemStack> items;

    public Menu(String title, Integer size, String layouts, Map<String, MenuIcon> icons) {
        this.title = title;
        this.size = size;
        this.layouts = layouts;
        this.icons = icons;
        create();
    }

    public record MenuIcon(String source, Map<String, ?> item, List<Map<?, ?>> functions) {

        public ItemStack parseItem(String key) {
            ItemSource self = ItemSource.getSource(key);
            return self.parseItem(item);
        }

    }

    /**
     * <p>创建一个菜单界面并添加物品</p>
     */
    private void create() {
        inventory = Bukkit.createInventory(this, size, title);
        final Map<Integer, ItemStack> items = MenuAPI.parseToInv(getLayouts(), getIcons());
        setItems(items);
        items.forEach((slot, item) -> getInventory().setItem(slot, item));
    }

    /**
     * <p>为玩家打开一个菜单界面</p>
     *
     * @param player 需打开界面的玩家
     */
    public void open(Player player) {
        player.closeInventory();
        player.openInventory(getInventory());
    }

    /**
     * <p>获取界面中的所有槽位id与对应物品堆</p>
     *
     * @return 所有槽位id与对应物品堆
     */
    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    /**
     * <p>设置界面中的所有槽位id与对应物品堆</p>
     *
     * @param items 数字与物品堆的map
     */
    public void setItems(Map<Integer, ItemStack> items) {
        this.items = items;
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
