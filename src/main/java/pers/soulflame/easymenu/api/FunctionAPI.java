package pers.soulflame.easymenu.api;

import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>物品功能相关api</p>
 */
public final class FunctionAPI {

    private FunctionAPI() {

    }

    private static final Map<String, ItemFunction> functions = new HashMap<>();

    /**
     * <p>添加物品功能</p>
     *
     * @param function 子类
     */
    public static void addFunction(ItemFunction function) {
        final var functions = getFunctions();
        functions.put(function.getKey(), function);
        final var add = EasyLoad.getPluginSec().getString("register.function",
                "&7新增物品功能&f: &c<key>").replace("<key>", function.getKey());
        TextUtil.sendMessage(add);
    }

    /**
     * <p>获取所有物品功能</p>
     *
     * @return 物品功能列表
     */
    public static Map<String, ItemFunction> getFunctions() {
        return functions;
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
