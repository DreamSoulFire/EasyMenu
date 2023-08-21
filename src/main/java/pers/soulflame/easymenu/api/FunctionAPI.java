package pers.soulflame.easymenu.api;

import pers.soulflame.easymenu.managers.ItemFunction;

import java.util.HashMap;
import java.util.Map;

public final class FunctionAPI {

    private FunctionAPI() {

    }

    /**
     * <p>获取所有物品功能</p>
     */
    private static final Map<String, ItemFunction> sources = new HashMap<>();

    /**
     * <p>添加物品功能</p>
     *
     * @param function 子类
     */
    public static void addFunction(ItemFunction function) {
        final Map<String, ItemFunction> functions = getFunctions();
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
}
