package pers.soulflame.easymenu.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import pers.soulflame.easymenu.api.MenuAPI;

import javax.script.*;
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
        engine.put("Player", Player.class);
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
     * @param script js脚本
     * @param uuid   玩家uuid
     * @return 返回值
     */
    public static boolean run(String script, UUID uuid) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        try {
            var eval = eval(script, uuid);
            if (script.startsWith("function")) {
                final var invocable = (Invocable) getEngine();
                script = script.substring(9, script.indexOf("("));
                final var check = invocable.invokeFunction(script, player);
                return parseBoolean(check);
            }
            return parseBoolean(eval);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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
     * @param script js脚本
     * @return 编译后的js
     */
    public static CompiledScript compile(String script) {
        try {
            final var compilable = (Compilable) getEngine();
            final var compiled = compilable.compile(script);
            compiledMap.put(script, compiled);
            return compiled;
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>执行js脚本</p>
     *
     * @param script 文本
     * @return 返回值
     */
    public static Object eval(String script, UUID uuid) {
        final var player = Bukkit.getPlayer(uuid);
        if (player != null)
            script = PlaceholderAPI.setPlaceholders(player, script);
        try {
            final var compiledScript = compiledMap.get(script);
            if (compiledScript == null) return compile(script).eval();
            return compiledScript.eval();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

}
