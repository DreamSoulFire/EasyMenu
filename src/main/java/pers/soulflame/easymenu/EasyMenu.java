package pers.soulflame.easymenu;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pers.soulflame.easymenu.commands.MainCommand;
import pers.soulflame.easymenu.listeners.PlayerCatchListener;
import pers.soulflame.easymenu.listeners.PlayerClickInvListener;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.managers.ItemSource;
import pers.soulflame.easymenu.managers.functions.CatchFunction;
import pers.soulflame.easymenu.managers.functions.CommandFunction;
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

    private void addFunction(ItemFunction function) {
        ItemFunction.addFunction(function);
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        TextUtil.sendMessage("&a开始加载插件");
        TextUtil.sendMessage("&a插件作者: &b" + getDescription().getAuthors());
        TextUtil.sendMessage("&a插件版本: &5" + getDescription().getVersion());

        addSource(new BaseSource("self"));
        addSource(new NISource("ni"));

        addFunction(new CatchFunction("catch"));
        addFunction(new CommandFunction("command"));

        try {
            FileUtil.loadAllFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TextUtil.sendMessage("&a开始注册监听...");
        register(new PlayerCatchListener());
        register(new PlayerClickInvListener());
        TextUtil.sendMessage("&a监听器注册完成");

        TextUtil.sendMessage("&a开始注册插件指令...");
        PluginCommand easymenu = getCommand("easymenu");
        if (easymenu != null) {
            easymenu.setExecutor(new MainCommand());
            easymenu.setTabCompleter(new MainCommand());
        }
        TextUtil.sendMessage("&a插件指令注册成功, 共加载了 &3" + MainCommand.getCommandMap().size() + " &a个子指令");

        TextUtil.sendMessage("&a插件加载完成, 感谢您的支持");
    }

    @Override
    public void onDisable() {
        TextUtil.sendMessage("&a插件已关闭, 感谢您的使用");
    }
}
