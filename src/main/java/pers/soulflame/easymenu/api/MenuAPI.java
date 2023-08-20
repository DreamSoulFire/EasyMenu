package pers.soulflame.easymenu.api;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pers.soulflame.easymenu.managers.Menu;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MenuAPI {

    private MenuAPI() {

    }

    /**
     * <p>从菜单yaml文件中加载菜单实例</p>
     *
     * @param yaml 需加载的yaml文件
     * @return 菜单实例
     */
    public static Menu loadMenu(YamlConfiguration yaml) {
        final String title = yaml.getString("title", "&f未配置菜单标题名");
        final Integer size = yaml.getInt("size", 54);
        final String layouts = yaml.getString("layouts", "").replace("\n", "");
        final ConfigurationSection icons = yaml.getConfigurationSection("icons");
        if (icons == null) throw new NullPointerException("Icons must not be null");
        final Map<String, Menu.MenuIcon> iconMap = new HashMap<>();
        for (final String key : icons.getKeys(false)) {
            final ConfigurationSection section = icons.getConfigurationSection(key);
            if (section == null) throw new NullPointerException("Icons must not be null");
            final String source = section.getString("source", "self");
            final List<Map<?, ?>> functions = section.getMapList("functions");
            final ConfigurationSection itemSec = section.getConfigurationSection("item");
            if (itemSec == null) {
                iconMap.put(key, new Menu.MenuIcon(source, new HashMap<>(), functions));
                continue;
            }
            final Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", itemSec.getString("name", null));
            itemMap.put("material", itemSec.getString("material", "STONE"));
            itemMap.put("lore", itemSec.getStringList("lore"));
            itemMap.put("custom-model-data", itemSec.getInt("custom-model-data", 0));
            itemMap.put("unbreakable", itemSec.getBoolean("unbreakable", false));
            ConfigurationSection enchantments = itemSec.getConfigurationSection("enchantments");
            final Map<String, Integer> enchMap = new HashMap<>();
            if (enchantments != null) {
                for (final String enchKey : enchantments.getKeys(false)) {
                    final int level = enchantments.getInt(enchKey, 1);
                    enchMap.put(enchKey, level);
                }
            }
            itemMap.put("enchantments", enchMap);
            itemMap.put("item-flags", itemSec.getStringList("item-flags"));
            iconMap.put(key, new Menu.MenuIcon(source, itemMap, functions));
        }
        return new Menu(TextUtil.color(title), size, layouts, iconMap);
    }

    /**
     * <p>解析layout为map</p>
     *
     * @param layouts 需解析的字符串
     * @return 解析后的map
     */
    public static Map<String, Integer> parseLayout(String layouts) {
        final Map<String, Integer> layoutMap = new HashMap<>();
        if (layouts.length() % 9 != 0) throw new NullPointerException("Inventory's size must not higher than 6 lines");
        int i = 0;
        final char[] charArray = layouts.toCharArray();
        for (int j = 0; j < charArray.length; i++, j++) {
            final char c = charArray[j];
            final String key = String.valueOf(c);
            if (key.isEmpty()) continue;
            layoutMap.put(key, i);
        }
        if (i > 54) throw new NullPointerException("Inventory's size must not higher than 54, but you set " + i);
        return layoutMap;
    }

    /**
     * <p>不好说明 反正是为了提高复用性(</p>
     *
     * @param layouts 界面排版字符串
     * @param icons 图标map
     * @param tempMap ?
     * @return ?
     * @param <T> ?
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<Integer, T> parse(String layouts, Map<String, Menu.MenuIcon> icons, Map<String, ?> tempMap) {
        final Map<Integer, T> map = new HashMap<>();
        final Map<String, Integer> layout = parseLayout(layouts);
        for (final String str : icons.keySet())
            map.put(layout.get(str), (T) tempMap.get(str));
        return map;
    }

    /**
     * <p>解析成便于inventory利用的map</p>
     *
     * @param layouts 排版字符串
     * @param icons 图标map
     * @return 数字和物品堆的map
     */
    public static Map<Integer, ItemStack> parseToInv(String layouts, Map<String, Menu.MenuIcon> icons) {
        return parse(layouts, icons, parseIconsItem(icons));
    }

    /**
     * <p>解析成数字与MenuIcon的map</p>
     * <p>便于监听器调用</p>
     *
     * @param layouts 排版字符串
     * @param icons 图标map
     * @return 数字与MenuIcon的map
     */
    public static Map<Integer, Menu.MenuIcon> parseIconsChar(String layouts, Map<String, Menu.MenuIcon> icons) {
        return parse(layouts, icons, icons);
    }

    /**
     * <p>将图标对应字符解析为物品</p>
     *
     * @param icons 图标map
     * @return 对应字符和物品堆的map
     */
    public static Map<String, ItemStack> parseIconsItem(Map<String, Menu.MenuIcon> icons) {
        final Map<String, ItemStack> itemMap = new HashMap<>();
        for (final Map.Entry<String, Menu.MenuIcon> entry : icons.entrySet()) {
            final String key = entry.getKey();
            final Menu.MenuIcon icon = entry.getValue();
            itemMap.put(key, icon.parseItem(icon.source()));
        }
        return itemMap;
    }
}
