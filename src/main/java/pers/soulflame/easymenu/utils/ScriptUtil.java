package pers.soulflame.easymenu.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import pers.soulflame.easymenu.api.MenuAPI;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

public class ScriptUtil {

    private static final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();

    /**
     * <p>执行js脚本</p>
     *
     * @param string 文本
     */
    public static void eval(String string) {
        engine.put("Bukkit", Bukkit.class);
        engine.put("Player", Player.class);
        engine.put("List", List.class);
        engine.put("Map", Map.class);
        engine.put("MenuAPI", MenuAPI.class);
        try {
            engine.eval(string);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

}
