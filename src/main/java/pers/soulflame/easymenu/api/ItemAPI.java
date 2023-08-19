package pers.soulflame.easymenu.api;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pers.soulflame.easymenu.menus.ItemSource;
import pers.soulflame.easymenu.menus.Menu;
import pers.soulflame.easymenu.utils.ItemUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemAPI {

    private ItemAPI() {

    }

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
            final ConfigurationSection itemSec = section.getConfigurationSection("item");
            if (itemSec == null) throw new NullPointerException("Icons must not be null");
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
            final List<Map<?, ?>> functions = section.getMapList("functions");
            iconMap.put(key, new Menu.MenuIcon(source, itemMap, functions));
        }
        return new Menu(TextUtil.color(title), size, layouts, iconMap);
    }

    public static Map<Integer, ItemStack> parseInventory(String layouts, Map<String, Menu.MenuIcon> icons) {
        final Map<String, ItemStack> iconMap = parseIconsItem(icons);
        final Map<Integer, ItemStack> items = new HashMap<>();
        if (layouts.length() % 9 != 0) throw new NullPointerException("Inventory's size must not higher than 6 lines");
        int i = 0;
        for (final char c : layouts.toCharArray()) {
            String key = String.valueOf(c);
            if (key.isEmpty()) {
                i++;
                continue;
            }
            ItemStack itemStack = iconMap.get(key);
            items.put(i++, itemStack);
        }
        if (i > 54) throw new NullPointerException("Inventory's size must not higher than 54");
        return items;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ItemStack> parseIconsItem(Map<String, Menu.MenuIcon> icons) {
        final Map<String, ItemStack> itemMap = new HashMap<>();
        for (final Map.Entry<String, Menu.MenuIcon> entry : icons.entrySet()) {
            final String key = entry.getKey();
            final Menu.MenuIcon icon = entry.getValue();
            if (!ItemSource.isSelfSource(icon.source())) continue;
            final Map<String, ?> map = icon.item();
            if (map == null) throw new NullPointerException("The config of Item must not be null");
            final Object material = map.get("material");
            if (material == null) throw new NullPointerException("Material must not be null");
            final String aMaterial = material.toString();
            if (aMaterial.contains(":"))
                throw new IllegalArgumentException("the character \":\" isn't a legal argument");
            final Material type = Material.matchMaterial(aMaterial);
            if (type == null) throw new NullPointerException("Material must not be null");
            final ItemStack item = new ItemStack(type);
            if (ItemUtil.itemAir(item)) throw new NullPointerException("Item must not be null");
            final ItemMeta meta = item.getItemMeta();
            if (meta == null) throw new NullPointerException("Item's meta must not be null");
            final Object name = map.get("name");
            if (name != null) {
                meta.setDisplayName(TextUtil.color(name.toString()));
            }
            final Object lore = map.get("lore");
            if (lore != null) {
                meta.setLore(TextUtil.color((List<String>) lore));
            }
            final Object customModelData = map.get("custom-model-data");
            if (customModelData != null) {
                meta.setCustomModelData((Integer) customModelData);
            }
            final Object enchantments = map.get("enchantments");
            if (enchantments != null) {
                final Map<String, Integer> enchants = (Map<String, Integer>) enchantments;
                enchants.forEach((ench, level) -> {
                    final NamespacedKey enchKey = NamespacedKey.minecraft(ench.toLowerCase());
                    final Enchantment enchantment = Enchantment.getByKey(enchKey);
                    if (enchantment == null) throw new NullPointerException("Enchantment must not be null");
                    item.addUnsafeEnchantment(enchantment, level);
                });
            }
            final Object itemFlags = map.get("item-flags");
            if (itemFlags != null) {
                final List<String> flags = (List<String>) itemFlags;
                for (final String flag : flags) meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
            }
            final Object unbreakable = map.get("unbreakable");
            if (unbreakable != null) {
                meta.setUnbreakable((Boolean) unbreakable);
            }
            item.setItemMeta(meta);
            itemMap.put(key, item);
        }
        return itemMap;
    }
}
