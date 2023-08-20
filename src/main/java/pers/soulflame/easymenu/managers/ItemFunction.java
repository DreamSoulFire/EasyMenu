package pers.soulflame.easymenu.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class ItemFunction {

    /**
     * <p>获取所有物品功能</p>
     */
    private static final Map<String, ItemFunction> sources = new HashMap<>();

    private final String key;

    public ItemFunction(String key) {
        this.key = key;
    }

    /**
     * <p>获取物品功能的内部名</p>
     *
     * @return 内部名字符串
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>添加物品功能</p>
     *
     * @param function 子类
     */
    public static void addFunction(ItemFunction function) {
        Map<String, ItemFunction> functions = getFunctions();
        functions.put(function.getKey(), function);
    }

    /**
     * <p>获取所有物品功能</p>
     *
     * @return 物品功能列表
     */
    public static Map<String, ItemFunction> getFunctions() {
        return sources;
    }

    /**
     * <p>获取与key对应的物品功能</p>
     *
     * @param key 物品功能的内部名
     * @return 物品功能
     */
    public static ItemFunction getFunction(String key) {
        return getFunctions().get(key);
    }

    protected abstract boolean run(Player player, String string);

}
