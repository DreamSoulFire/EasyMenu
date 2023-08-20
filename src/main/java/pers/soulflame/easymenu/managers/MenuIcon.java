package pers.soulflame.easymenu.managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.soulflame.easymenu.managers.functions.CatchFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MenuIcon(String source, Map<String, ?> item, List<Map<?, ?>> functions) {

    private record Result(ItemFunction function, List<Map<?, ?>> funcMap) {

    }

    private static final Map<Player, Result> tempMap = new HashMap<>();

    /**
     * <p>根据物品源解析</p>
     *
     * @param key 物品源的内部名
     * @return 解析后的物品堆
     */
    public ItemStack parseItem(String key) {
        final ItemSource self = ItemSource.getSource(key);
        if (self == null) {
            throw new NullPointerException("Item source must not be null");
        }
        return self.parseItem(item);
    }

    /**
     * <p>执行物品功能</p>
     *
     * @param player 需执行的玩家
     */
    public void runFunctions(Player player) {
        if (functions == null || functions.isEmpty()) return;
        ItemFunction itemFunction = null;
        final List<Map<?, ?>> temp = new ArrayList<>(functions);
        for (final Map<?, ?> function : functions) {
            final Object type = function.get("type");
            if (type == null) continue;
            itemFunction = ItemFunction.getFunction((String) type);
            if (itemFunction == null) continue;
            final Object value = function.get("value");
            if (value == null) continue;
            if (!itemFunction.run(player, (String) value)) break;
            if (!CatchFunction.catches.contains(player)) continue;
            break;
        }
        if (itemFunction == null) return;
        temp.remove(0);
        tempMap.put(player, new Result(itemFunction, temp));
    }

    /**
     * <p>执行捕获聊天信息后的物品功能</p>
     *
     * @param player 玩家
     */
    public static void runAfterCatch(Player player) {
        final Result result = tempMap.get(player);
        if (result == null) return;
        ItemFunction itemFunction = result.function();
        if (itemFunction == null) return;
        final List<Map<?, ?>> funList = result.funcMap();
        if (funList == null || funList.isEmpty()) return;
        for (final Map<?, ?> function : funList) {
            final Object type = function.get("type");
            if (type == null) continue;
            itemFunction = ItemFunction.getFunction((String) type);
            if (itemFunction == null) continue;
            final Object value = function.get("value");
            if (value == null) continue;
            String str = (String) value;
            final String catchMsg = CatchFunction.tempMap.get(player);
            if (catchMsg != null) str = str.replace("$catch", catchMsg);
            if (!itemFunction.run(player, str)) break;
        }
    }

}
