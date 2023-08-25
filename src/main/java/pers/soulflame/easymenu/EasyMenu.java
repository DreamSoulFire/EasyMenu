package pers.soulflame.easymenu;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pers.soulflame.easymenu.commands.MainCommand;
import pers.soulflame.easymenu.listeners.PlayerCatchListener;
import pers.soulflame.easymenu.listeners.PlayerClickInvListener;
import pers.soulflame.easymenu.metrics.Metrics;
import pers.soulflame.easymenu.utils.TextUtil;

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


    @Override
    public void onLoad() {
        instance = this;
        new Metrics(this, 15780);
    }

    @Override
    public void onEnable() {
        EasyLoad.init();

        final var section = EasyLoad.getPluginSec();
        if (section == null) throw new NullPointerException("Plugin section in language file must not be null");
        TextUtil.sendMessage(section.getStringList("listeners.start"));
        register(new PlayerCatchListener());
        register(new PlayerClickInvListener());
        TextUtil.sendMessage(section.getStringList("listeners.finish"));

        TextUtil.sendMessage(section.getStringList("commands.start"));
        final var easymenu = getCommand("easymenu");
        if (easymenu != null) {
            easymenu.setExecutor(new MainCommand());
            easymenu.setTabCompleter(new MainCommand());
        }
        final var command = section.getStringList("commands.finish").stream().map(string ->
                string.replace("<amount>", String.valueOf(EasyLoad.getCommandMap().size()))).toList();
        TextUtil.sendMessage(command);

        TextUtil.sendMessage(section.getStringList("finish"));
    }

    @Override
    public void onDisable() {
        final var close = EasyLoad.getPluginSec().getStringList("close").stream().map(string ->
                        string.replace("<author>", getDescription().getAuthors().toString())
                                .replace("<version>", getDescription().getVersion())).toList();
        TextUtil.sendMessage(close);
    }
}
