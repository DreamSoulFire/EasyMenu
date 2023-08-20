package pers.soulflame.easymenu.managers.sources;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pers.soulflame.easymenu.managers.ItemSource;
import pers.soulflame.easymenu.utils.ItemUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.List;
import java.util.Map;

public class BaseSource extends ItemSource {

    public BaseSource() {
        super("self");
    }

    /**
     * <p>通过本插件直接解析物品</p>
     *
     * @param map 被解析的map
     * @return 物品堆
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ItemStack parseItem(Map<String, ?> map) {
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
                item.addEnchantment(enchantment, level);
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
        return item;
    }
}
