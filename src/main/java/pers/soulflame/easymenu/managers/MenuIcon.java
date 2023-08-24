package pers.soulflame.easymenu.managers;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import pers.soulflame.easymenu.api.FunctionAPI;
import pers.soulflame.easymenu.api.SourceAPI;
import pers.soulflame.easymenu.managers.functions.CatchFunction;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public record MenuIcon(String source, Map<String, ?> item, List<Map<String, ?>> functions) {


    /**
     * <p>缓存类</p>
     *
     * @param funcMap
     * @param clickType
     */
    private record Result(List<Map<String, ?>> funcMap, ClickType clickType) {

    }

    private static final Map<UUID, Result> tempMap = new HashMap<>();

    /**
     * <p>根据物品源解析</p>
     *
     * @param uuid 玩家uuid
     * @param key  物品源的内部名
     * @return 解析后的物品堆
     */
    public ItemStack parseItem(UUID uuid, String key) {
        final var self = SourceAPI.getSource(key);
        if (self == null) {
            throw new NullPointerException("Item source must not be null");
        }
        return self.parseItem(uuid, item);
    }

    /**
     * <p>执行物品功能</p>
     *
     * @param uuid 需执行的玩家
     */
    public void runFunctions(UUID uuid, ClickType clickType) {
        if (functions == null || functions.isEmpty()) return;
        var isContent = true;
        final var i = new AtomicInteger(0);
        for (final var function : functions) {
            final var click = (String) function.get("click");
            if (click != null) {
                ClickType type = ClickType.valueOf(click.toUpperCase());
                if (!type.equals(clickType)) continue;
            }
            final var condition = function.get("condition");
            if (condition != null) {
                isContent = ScriptUtil.run((String) condition, uuid);
            }
            if (!isContent) continue;
            final var type = function.get("type");
            if (type == null) continue;
            final var itemFunction = FunctionAPI.getFunction((String) type);
            if (itemFunction == null) continue;
            final var yaml = FileUtil.getYaml("config.yml");
            final var aCatch = yaml.getString("functions.catch", "catch");
            if (itemFunction.getKey().equals(aCatch)) {
                i.set(functions.indexOf(function));
            }
            final var value = function.get("value");
            if (value == null) continue;
            if (!itemFunction.run(uuid, (String) value)) break;
            if (CatchFunction.catches.contains(uuid)) break;
        }
        final var temp = functions.stream().filter(map -> functions.indexOf(map) > i.get()).toList();
        tempMap.put(uuid, new Result(temp, clickType));
    }

    /**
     * <p>执行捕获聊天信息后的物品功能</p>
     *
     * @param uuid 玩家
     */
    public static void runAfterCatch(UUID uuid) {
        final var result = tempMap.get(uuid);
        if (result == null) return;
        final var funList = result.funcMap();
        if (funList == null || funList.isEmpty()) return;
        var isContent = true;
        for (final var function : funList) {
            final var click = (String) function.get("click");
            if (click != null) {
                ClickType type = ClickType.valueOf(click.toUpperCase());
                if (!type.equals(result.clickType())) continue;
            }
            final var condition = function.get("condition");
            if (condition != null) {
                isContent = ScriptUtil.run((String) condition, uuid);
            }
            if (!isContent) continue;
            final var type = function.get("type");
            if (type == null) continue;
            final var itemFunction = FunctionAPI.getFunction((String) type);
            if (itemFunction == null) continue;
            final var value = function.get("value");
            if (value == null) continue;
            var str = (String) value;
            final var catchMsg = CatchFunction.tempMap.get(uuid);
            if (catchMsg != null) str = str.replace("$catch", catchMsg);
            if (!itemFunction.run(uuid, str)) break;
        }
    }

}
