package pers.soulflame.easymenu.api;

import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.managers.ItemCondition;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>物品条件相关api</p>
 */
public class ConditionAPI {

    private ConditionAPI() {

    }

    private static final Map<String, ItemCondition> conditions = new HashMap<>();

    /**
     * <p>添加物品条件</p>
     *
     * @param condition 子类
     */
    public static void addCondition(ItemCondition condition) {
        final var functions = getConditions();
        functions.put(condition.getKey(), condition);
        final var add = EasyLoad.getPluginSec().getString("register.condition",
                "&7新增物品条件&f: &2<key>").replace("<key>", condition.getKey());
        TextUtil.sendMessage(add);
    }

    /**
     * <p>获取所有物品条件</p>
     *
     * @return 物品条件列表
     */
    public static Map<String, ItemCondition> getConditions() {
        return conditions;
    }

    /**
     * <p>获取与key对应的物品条件</p>
     *
     * @param key 物品条件的内部名
     * @return 物品条件
     */
    public static ItemCondition getCondition(String key) {
        return getConditions().get(key);
    }
}
