package pers.soulflame.easymenu.managers.sources;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pers.soulflame.easymenu.managers.ItemSource;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BaseSource extends ItemSource {

    public BaseSource(String key) {
        super(key);
    }

    /**
     * <p>通过本插件直接解析物品</p>
     *
     * @param uuid 解析变量的玩家
     * @param map  被解析的map
     * @return 物品堆
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ItemStack parseItem(UUID uuid, Map<String, ?> map) {
        Player player = null;
        if (uuid != null) player = Bukkit.getPlayer(uuid);
        if (map == null) throw new NullPointerException("The config of Item must not be null");
        final Object material = map.get("material");
        if (material == null) throw new NullPointerException("Material must not be null");
        final String aMaterial = material.toString();
        if (aMaterial.contains(":"))
            throw new IllegalArgumentException("the character \":\" is a illegal argument");
        final Material type = Material.matchMaterial(aMaterial);
        if (type == null) throw new NullPointerException("Wrong material of item " + aMaterial);
        final ItemStack item = new ItemStack(type);
        if (Material.AIR.equals(item.getType()) || item.getAmount() <= 0)
            throw new NullPointerException("Item must not be null");
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("Item's meta must not be null");
        final Object name = map.get("name");
        if (name != null) {
            String displayName = player == null ? name.toString() :
                    PlaceholderAPI.setPlaceholders(player, name.toString());
            meta.setDisplayName(TextUtil.color(displayName));
        }
        final Object lore = map.get("lore");
        if (lore != null) {
            List<String> lores = player == null ? (List<String>) lore :
                    PlaceholderAPI.setPlaceholders(player, (List<String>) lore);
            meta.setLore(TextUtil.color(lores));
        }
        final Object customModelData = map.get("custom-model-data");
        if (customModelData != null)
            meta.setCustomModelData((Integer) customModelData);
        final Object enchantments = map.get("enchantments");
        if (enchantments != null) {
            final Map<String, Integer> enchants = (Map<String, Integer>) enchantments;
            enchants.forEach((ench, level) -> {
                final NamespacedKey enchKey = NamespacedKey.minecraft(ench.toLowerCase());
                final Enchantment enchantment = Enchantment.getByKey(enchKey);
                if (enchantment == null) throw new NullPointerException("Enchantment must not be null, but you set " + ench);
                meta.addEnchant(enchantment, level, true);
            });
        }
        final Object itemFlags = map.get("item-flags");
        if (itemFlags != null) {
            final List<String> flags = (List<String>) itemFlags;
            for (final String flag : flags) meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
        }
        final Object unbreakable = map.get("unbreakable");
        if (unbreakable != null)
            meta.setUnbreakable((Boolean) unbreakable);
        item.setItemMeta(meta);
        return item;
    }
}
