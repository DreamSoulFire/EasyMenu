package pers.soulflame.easymenu.menus;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>物品来源</p>
 */
public class ItemSource {

    /**
     * <p>获取所有物品来源</p>
     */
    private static final List<String> sources = new ArrayList<>();

    private String key;

    static  {
        sources.add("self");
        sources.add("ni");
    }

    public ItemSource(String key) {
        this.key = key;
        addItemSource(key);
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
     * <p>设置物品源的内部名</p>
     *
     * @param key 内部名
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * <p>添加物品来源</p>
     *
     * @param source 子类
     */
    public static void addItemSource(String source) {
        if (getSources().contains(source)) return;
        getSources().add(source);
    }

    /**
     * <p>获取所有物品源</p>
     *
     * @return 物品源列表
     */
    public static List<String> getSources() {
        return sources;
    }

    /**
     * <p>判断物品源是否为自己</p>
     *
     * @param source 物品源
     * @return 是否为自己
     */
    public static boolean isSelfSource(String source) {
        if (source == null) return false;
        return source.equalsIgnoreCase("self");
    }

}
