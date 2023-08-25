package pers.soulflame.easymenu.api;

import org.bukkit.inventory.ItemStack;
import pers.soulflame.easymenu.managers.Menu;
import pers.soulflame.easymenu.managers.MenuIcon;
import pers.soulflame.easymenu.utils.TextUtil;
import pers.soulflame.easymenu.utils.YamlUtil;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>菜单相关api</p>
 */
public final class MenuAPI {

    private MenuAPI() {

    }


    /**
     * <p>缓存类</p>
     *
     * @param string 字符
     * @param item 图标物品
     * @param icon 图标对象
     */
    public record Result(String string, ItemStack item, MenuIcon icon) {

    }


    /**
     * <p>从菜单yaml文件中加载菜单实例</p>
     *
     * @param path 需加载的yaml文件路径
     * @return 菜单实例
     */
    @SuppressWarnings("unchecked")
    public static Menu loadMenu(String path) {
        Menu menu = null;
        try (final var reader = new FileReader(path, StandardCharsets.UTF_8)) {
            final var menuMap = YamlUtil.loadAs(reader, Map.class);
            final var title = TextUtil.color(((String) menuMap.get("title")));
            final var size = (Integer) menuMap.get("size");
            final var options = (Map<String, Object>) menuMap.get("condition");
            final var layouts = ((String) menuMap.get("layouts")).replace("\n", "");
            final var icons = (Map<String, Object>) menuMap.get("icons");
            menu = new Menu(title, size, options, layouts, icons);
        } catch (IOException ignored) {
        }
        return menu;
    }

    /**
     * <p>解析成便于inventory利用的map</p>
     *
     * @param layouts 排版字符串
     * @param icons   图标map
     * @param uuid    玩家uuid
     * @return 数字和物品堆的map
     */
    @SuppressWarnings("unchecked")
    public static Map<Integer, Result> parseInv(String layouts, Map<String, Object> icons, UUID uuid) {
        final Map<Integer, Result> map = new HashMap<>();
        if (layouts.length() % 9 != 0) throw new NullPointerException("Inventory's size must not higher than 6 lines");
        final var i = new AtomicInteger();
        for (char c : layouts.toCharArray()) {
            final var key = String.valueOf(c);
            final var iconMap = (Map<String, Object>) icons.get(key);
            final var source = (String) iconMap.get("source");
            final var item = (Map<String, Object>) iconMap.get("item");
            final var functions = (List<Map<String, ?>>) iconMap.get("functions");
            final var menuIcon = new MenuIcon(source, item, functions);
            final var parseItem = menuIcon.parseItem(uuid, source);
            map.put(i.getAndIncrement(), new Result(key, parseItem, menuIcon));
        }
        if (i.get() > 54) throw new NullPointerException("Inventory's size must not higher than 54, but you set '" + i.get() + "'");
        return map;
    }

    /**
     * <p>获取map中的menuIcon</p>
     *
     * @param resultMap map
     * @param slot      槽位id
     * @return menuIcon
     */
    public static MenuIcon getMenuIcon(Map<Integer, Result> resultMap, Integer slot) {
        var iconResult = resultMap.get(slot);
        return iconResult.icon();
    }
}
