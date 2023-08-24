package pers.soulflame.easymenu.managers;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * <p>物品来源</p>
 */
public abstract class ItemSource {

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
     * <p>解析item为物品</p>
     *
     * @param uuid 玩家uuid
     * @param map 配置中的物品map
     * @return 物品堆
     */
    protected abstract ItemStack parseItem(UUID uuid, Map<String, ?> map);

}
