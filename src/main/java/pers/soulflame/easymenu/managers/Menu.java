package pers.soulflame.easymenu.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * <p>菜单界面构造类</p>
 */
public class Menu implements InventoryHolder {

    private final String title;
    private final Integer size;
    private final String layouts;
    private final Map<String, MenuIcon> icons;

    private static Inventory inventory;

    public Menu(String title, Integer size, String layouts, Map<String, MenuIcon> icons) {
        this.title = title;
        this.size = size;
        this.layouts = layouts;
        this.icons = icons;
        inventory = Bukkit.createInventory(this, size, title);
    }

    public String title() {
        return title;
    }

    public Integer size() {
        return size;
    }

    public String layouts() {
        return layouts;
    }

    public Map<String, MenuIcon> icons() {
        return icons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;

        Menu menu = (Menu) o;

        if (!title.equals(menu.title)) return false;
        if (!size.equals(menu.size)) return false;
        if (!Objects.equals(layouts, menu.layouts)) return false;
        return Objects.equals(icons, menu.icons);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + size.hashCode();
        result = 31 * result + (layouts != null ? layouts.hashCode() : 0);
        result = 31 * result + (icons != null ? icons.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "title='" + title + '\'' +
                ", size=" + size +
                ", layouts='" + layouts + '\'' +
                ", icons=" + icons +
                '}';
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

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
