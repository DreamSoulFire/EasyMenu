package pers.soulflame.easymenu.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import pers.soulflame.easymenu.api.MenuAPI;

import javax.script.*;
import java.util.*;

public class ScriptUtil {

    private static final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();

    static {
        engine.put("ArrayList", ArrayList.class);
        engine.put("Arrays", Arrays.class);
        engine.put("List", List.class);
        engine.put("Map", Map.class);
        engine.put("Math", Math.class);
        engine.put("Bukkit", Bukkit.class);
        engine.put("Player", Player.class);
        engine.put("ItemStack", ItemStack.class);
        engine.put("Material", Material.class);
        engine.put("MenuAPI", MenuAPI.class);
        engine.put("TextUtil", TextUtil.class);
    }

    private static final Map<String, CompiledScript> scriptMap = new HashMap<>();

    /**
     * <p>获取脚本引擎</p>
     *
     * @return 引擎
     */
    public static ScriptEngine getEngine() {
        return engine;
    }

    /**
     * <p>获取预编译后的js</p>
     *
     * @return 预编译后的js
     */
    public static Map<String, CompiledScript> getScriptMap() {
        return scriptMap;
    }

    /**
     * <p>用于条件判断</p>
     *
     * @param script js脚本
     * @param player 玩家
     * @return 返回值
     */
    public static boolean check(String script, Player player) {
        eval(script);
        final Invocable invocable = (Invocable) getEngine();
        try {
            final Object check = invocable.invokeFunction("check", player);
            if (!(check instanceof final Boolean result)) return false;
            if (result != Boolean.FALSE && result != Boolean.TRUE) return false;
            return result;
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>执行js脚本</p>
     *
     * @param script 文本
     * @return 返回值
     */
    public static Object eval(String script) {
        final Map<String, CompiledScript> tempMap = getScriptMap();
        final CompiledScript temp = tempMap.get(script);
        try {
            if (temp == null) {
                final Compilable compilable = (Compilable) getEngine();
                final CompiledScript compiledScript = compilable.compile(script);
                tempMap.put(script, compiledScript);
                return compiledScript.eval();
            }
            return temp.eval();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

}
