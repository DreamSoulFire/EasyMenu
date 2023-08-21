package pers.soulflame.easymenu.api;

import pers.soulflame.easymenu.managers.ItemSource;

import java.util.HashMap;
import java.util.Map;

public final class SourceAPI {

    private SourceAPI() {

    }

    /**
     * <p>获取所有物品来源</p>
     */
    private static final Map<String, ItemSource> sources = new HashMap<>();

    /**
     * <p>添加物品来源</p>
     *
     * @param source 子类
     */
    public static void addSource(ItemSource source) {
        final Map<String, ItemSource> itemSources = getSources();
        itemSources.put(source.getKey(), source);
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
}
