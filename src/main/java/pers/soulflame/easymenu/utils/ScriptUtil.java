package pers.soulflame.easymenu.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import pers.soulflame.easymenu.api.MenuAPI;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class ScriptUtil {

    private static final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();

    static {
        engine.put("ArrayList", ArrayList.class);
        engine.put("Arrays", Arrays.class);
        engine.put("List", List.class);
        engine.put("Map", Map.class);
        engine.put("Math", Math.class);
        engine.put("String", String.class);
        engine.put("Bukkit", Bukkit.class);
        engine.put("ItemStack", ItemStack.class);
        engine.put("Material", Material.class);
        engine.put("MenuAPI", MenuAPI.class);
        engine.put("TextUtil", TextUtil.class);
    }

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
     * <p>用于条件判断</p>
     *
     * @param file js文件名
     * @param uuid 玩家uuid
     * @return 返回值
     */
    public static boolean run(String file, UUID uuid) {
        return parseBoolean(eval(file, uuid));
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
     * @param file js文件名
     * @return 编译后的js
     */
    public static CompiledScript compile(String file) {
        CompiledScript compiled = null;
        final var compilable = (Compilable) getEngine();
        try (final var reader = new FileReader(file, StandardCharsets.UTF_8)) {
            final var script = YamlUtil.loadAs(reader, String.class);
            compiled = compilable.compile(script);
            compiledMap.put(file, compiled);
        } catch (IOException ignored) {
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
    public static boolean eval(String script, Player player) {
        final var bindings = getEngine().createBindings();
        bindings.put("player", player);
        try {
            return parseBoolean(getEngine().eval(script, bindings));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>执行js脚本</p>
     *
     * @param file js文件名
     * @return 返回值
     */
    public static Object eval(String file, UUID uuid) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return null;
        final var bindings = getEngine().createBindings();
        bindings.put("player", player);
        try {
            final var compiledScript = compiledMap.get(file);
            if (compiledScript == null)
                return compile(file).eval(bindings);
            return compiledScript.eval(bindings);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

}
