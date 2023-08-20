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

    public ItemStack parseItem(String key) {
        final ItemSource self = ItemSource.getSource(key);
        return self.parseItem(item);
    }

    public void runFunctions(Player player) {
        if (functions == null || functions.isEmpty()) return;
        ItemFunction itemFunction = null;
        final List<Map<?, ?>> temp = new ArrayList<>(functions);
        for (final Map<?, ?> function : functions) {
//                Object condition = function.get("condition");
//                if (condition == null) continue;
            final Object type = function.get("type");
            if (type == null) continue;
            itemFunction = ItemFunction.getFunction((String) type);
            if (itemFunction == null) continue;
            final Object value = function.get("value");
            if (value == null) continue;
            itemFunction.run(player, (String) value);
            if (!CatchFunction.catches.contains(player)) continue;
            break;
        }
        if (itemFunction == null) return;
        temp.remove(0);
        tempMap.put(player, new Result(itemFunction, temp));
    }

    public static void runAfterCatch(Player player) {
        final Result result = tempMap.get(player);
        if (result == null) return;
        ItemFunction itemFunction = result.function();
        if (itemFunction == null) return;
        final List<Map<?, ?>> funList = result.funcMap();
        if (funList == null || funList.isEmpty()) return;
        for (final Map<?, ?> function : funList) {
//                Object condition = function.get("condition");
//                if (condition == null) continue;
            final Object type = function.get("type");
            if (type == null) continue;
            itemFunction = ItemFunction.getFunction((String) type);
            if (itemFunction == null) continue;
            final Object value = function.get("value");
            if (value == null) continue;
            String str = (String) value;
            final String catchMsg = CatchFunction.tempMap.get(player);
            if (catchMsg != null) str = str.replace("$catch", catchMsg);
            itemFunction.run(player, str);
        }
    }

}
