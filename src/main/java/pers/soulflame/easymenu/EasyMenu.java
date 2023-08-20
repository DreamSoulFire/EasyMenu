package pers.soulflame.easymenu;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pers.soulflame.easymenu.commands.MainCommand;
import pers.soulflame.easymenu.listeners.PlayerCatchListener;
import pers.soulflame.easymenu.listeners.PlayerClickInvListener;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.managers.ItemSource;
import pers.soulflame.easymenu.managers.functions.CatchFunction;
import pers.soulflame.easymenu.managers.functions.CommandFunction;
import pers.soulflame.easymenu.managers.functions.JSFunction;
import pers.soulflame.easymenu.managers.functions.PointsFunction;
import pers.soulflame.easymenu.managers.sources.BaseSource;
import pers.soulflame.easymenu.managers.sources.NISource;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.io.IOException;

/**
 * <p>插件主类</p>
 */
public final class EasyMenu extends JavaPlugin {

    private static Plugin instance;

    /**
     * <p>获取插件实例</p>
     *
     * @return 实例
     */
    public static Plugin getInstance() {
        return instance;
    }

    /**
     * <p>注册监听器</p>
     *
     * @param listener 监听器
     */
    private void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    /**
     * <p>添加物品源</p>
     *
     * @param source 物品源
     */
    private void addSource(ItemSource source) {
        ItemSource.addSource(source);
    }

    /**
     * <p>添加物品功能</p>
     *
     * @param function 物品功能
     */
    private void addFunction(ItemFunction function) {
        ItemFunction.addFunction(function);
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        addSource(new BaseSource("self"));
        PluginManager manager = Bukkit.getPluginManager();
        Plugin neigeItems = manager.getPlugin("NeigeItems");
        if (neigeItems != null) addSource(new NISource("ni"));

        addFunction(new CatchFunction("catch"));
        addFunction(new CommandFunction("command"));
        addFunction(new JSFunction("js"));
        Plugin playerPoints = manager.getPlugin("PlayerPoints");
        if (playerPoints != null) addFunction(new PointsFunction("points"));

        try {
            FileUtil.loadAllFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        register(new PlayerCatchListener());
        register(new PlayerClickInvListener());

        PluginCommand easymenu = getCommand("easymenu");
        if (easymenu != null) {
            easymenu.setExecutor(new MainCommand());
            easymenu.setTabCompleter(new MainCommand());
        }

        TextUtil.startInfo();
    }

    @Override
    public void onDisable() {
        TextUtil.sendMessage(FileUtil.getLanguage().getStringList("plugin.close"));
    }
}
