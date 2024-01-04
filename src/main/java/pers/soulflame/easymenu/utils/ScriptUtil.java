package pers.soulflame.easymenu.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import pers.soulflame.easymenu.api.MenuAPI;

import javax.script.*;
import java.util.*;

public final class ScriptUtil {

    private static final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();

    private static final Map<String, CompiledScript> compiledMap = new HashMap<>();

    /**
     * <p>获取脚本引擎</p>
     *
     * @return 引擎
     */
    public static ScriptEngine getEngine() {
        return engine;
    }

    /**
     * <p>检测对象是否为布尔类型</p>
     *
     * @param js 对象
     * @return 布尔值
     */
    public static boolean parseBoolean(Object js) {
        if (!(js instanceof Boolean result)) return false;
        if (!Boolean.TRUE.equals(result) && !Boolean.FALSE.equals(result)) return false;
        return result;
    }

    /**
     * <p>预编译js脚本</p>
     *
     * @param script js
     * @return 编译后的js
     */
    public static CompiledScript compile(String script) {
        CompiledScript compiled;
        final var compilable = (Compilable) getEngine();
        try {
            compiled = compilable.compile(script);
            compiled.eval();
            compiledMap.put(script, compiled);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        return compiled;
    }

    /**
     * <p>执行js脚本</p>
     *
     * @param script js
     * @return 返回值
     */
    public static boolean eval(String script, UUID uuid) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        script = PlaceholderAPI.setPlaceholders(player, script);
        final var bindings = getEngine().createBindings();
        bindings.put("player", player);
        putBindings(bindings);
        try {
            final var compiledScript = compiledMap.get(script);
            if (compiledScript == null)
                return parseBoolean(compile(script).eval(bindings));
            return parseBoolean(compiledScript.eval(bindings));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>导入工具类</p>
     *
     * @param bindings 需导入的bindings
     */
    private static void putBindings(Bindings bindings) {
        bindings.put("ArrayList", ArrayList.class);
        bindings.put("Arrays", Arrays.class);
        bindings.put("List", List.class);
        bindings.put("Map", Map.class);
        bindings.put("Math", Math.class);
        bindings.put("String", String.class);
        bindings.put("Integer", Integer.class);
        bindings.put("Bukkit", Bukkit.class);
        bindings.put("ItemStack", ItemStack.class);
        bindings.put("Material", Material.class);
        bindings.put("MenuAPI", MenuAPI.class);
        bindings.put("PlaceholderAPI", PlaceholderAPI.class);
        bindings.put("TextUtil", TextUtil.class);
    }

}
