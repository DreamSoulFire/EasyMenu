package pers.soulflame.easymenu.managers;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>物品来源</p>
 */
public abstract class ItemSource {

    /**
     * <p>获取所有物品来源</p>
     */
    private static final Map<String, ItemSource> sources = new HashMap<>();

    private final String key;

    public ItemSource(String key) {
        this.key = key;
    }

    /**
     * <p>获取物品源的内部名</p>
     *
     * @return 内部名字符串
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>添加物品来源</p>
     *
     * @param source 子类
     */
    public static void addItemSource(ItemSource source) {
        if (getSources().containsValue(source)) return;
        getSources().put(source.getKey(), source);
    }

    /**
     * <p>获取所有物品源</p>
     *
     * @return 物品源列表
     */
    public static Map<String, ItemSource> getSources() {
        return sources;
    }

    /**
     * <p>获取与key对应的物品源</p>
     *
     * @param key 物品源的内部名
     * @return 物品源
     */
    public static ItemSource getSource(String key) {
        return getSources().get(key);
    }

    protected abstract ItemStack parseItem(Map<String, ?> map);

}
