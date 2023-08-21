package pers.soulflame.easymenu.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.soulflame.easymenu.api.FunctionAPI;
import pers.soulflame.easymenu.api.SourceAPI;
import pers.soulflame.easymenu.managers.functions.CatchFunction;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.*;

public class MenuIcon {

    private final String source;
    private final Map<String, ?> item;
    private final List<Map<?, ?>> functions;

    public MenuIcon(String source, Map<String, ?> item, List<Map<?, ?>> functions) {
        this.source = source;
        this.item = item;
        this.functions = functions;
    }

    public String source() {
        return source;
    }

    public Map<String, ?> item() {
        return item;
    }

    public List<Map<?, ?>> functions() {
        return functions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuIcon)) return false;

        MenuIcon menuIcon = (MenuIcon) o;

        if (!Objects.equals(source, menuIcon.source)) return false;
        if (!Objects.equals(item, menuIcon.item)) return false;
        return Objects.equals(functions, menuIcon.functions);
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (functions != null ? functions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MenuIcon{" +
                "source='" + source + '\'' +
                ", item=" + item +
                ", functions=" + functions +
                '}';
    }

    private static class Result {
        private final ItemFunction function;
        private final List<Map<?, ?>> funcMap;

        public Result(ItemFunction function, List<Map<?, ?>> funcMap) {
            this.function = function;
            this.funcMap = funcMap;
        }

        public ItemFunction function() {
            return function;
        }

        public List<Map<?, ?>> funcMap() {
            return funcMap;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Result)) return false;

            Result result = (Result) o;

            if (!Objects.equals(function, result.function)) return false;
            return Objects.equals(funcMap, result.funcMap);
        }

        @Override
        public int hashCode() {
            int result = function != null ? function.hashCode() : 0;
            result = 31 * result + (funcMap != null ? funcMap.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "function=" + function +
                    ", funcMap=" + funcMap +
                    '}';
        }
    }

    private static final Map<UUID, Result> tempMap = new HashMap<>();

    /**
     * <p>根据物品源解析</p>
     *
     * @param uuid 玩家uuid
     * @param key 物品源的内部名
     * @return 解析后的物品堆
     */
    public ItemStack parseItem(UUID uuid, String key) {
        final ItemSource self = SourceAPI.getSource(key);
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
    public void runFunctions(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (functions == null || functions.isEmpty()) return;
        ItemFunction itemFunction = null;
        final List<Map<?, ?>> temp = new ArrayList<>(functions);
        boolean isContent = true;
        for (final Map<?, ?> function : functions) {
            final Object condition = function.get("condition");
            if (condition != null) {
                isContent = ScriptUtil.check((String) condition, player);
            }
            if (!isContent) continue;
            final Object type = function.get("type");
            if (type == null) continue;
            itemFunction = FunctionAPI.getFunction((String) type);
            if (itemFunction == null) continue;
            final Object value = function.get("value");
            if (value == null) continue;
            if (!itemFunction.run(uuid, (String) value)) break;
            if (CatchFunction.catches.contains(uuid)) break;
        }
        if (itemFunction == null) return;
        temp.remove(0);
        tempMap.put(uuid, new Result(itemFunction, temp));
    }

    /**
     * <p>执行捕获聊天信息后的物品功能</p>
     *
     * @param uuid 玩家
     */
    public static void runAfterCatch(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        final Result result = tempMap.get(uuid);
        if (result == null) return;
        ItemFunction itemFunction = result.function();
        if (itemFunction == null) return;
        final List<Map<?, ?>> funList = result.funcMap();
        if (funList == null || funList.isEmpty()) return;
        boolean isContent = true;
        for (final Map<?, ?> function : funList) {
            final Object condition = function.get("condition");
            if (condition != null) {
                isContent = ScriptUtil.check((String) condition, player);
            }
            if (!isContent) continue;
            final Object type = function.get("type");
            if (type == null) continue;
            itemFunction = FunctionAPI.getFunction((String) type);
            if (itemFunction == null) continue;
            final Object value = function.get("value");
            if (value == null) continue;
            String str = (String) value;
            final String catchMsg = CatchFunction.tempMap.get(uuid);
            if (catchMsg != null) str = str.replace("$catch", catchMsg);
            if (!itemFunction.run(uuid, str)) break;
        }
    }

}
