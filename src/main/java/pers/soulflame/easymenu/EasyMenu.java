package pers.soulflame.easymenu;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pers.soulflame.easymenu.commands.MainCommand;
import pers.soulflame.easymenu.listeners.PlayerClickInvListener;
import pers.soulflame.easymenu.utils.FileUtil;

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

    private void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        try {
            FileUtil.loadAllFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        register(new PlayerClickInvListener());

        PluginCommand easymenu = getCommand("easymenu");
        if (easymenu != null) {
            easymenu.setExecutor(new MainCommand());
            easymenu.setTabCompleter(new MainCommand());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
