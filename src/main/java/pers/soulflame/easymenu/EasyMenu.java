package pers.soulflame.easymenu;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pers.soulflame.easymenu.api.FunctionAPI;
import pers.soulflame.easymenu.api.SourceAPI;
import pers.soulflame.easymenu.commands.MainCommand;
import pers.soulflame.easymenu.listeners.PlayerCatchListener;
import pers.soulflame.easymenu.listeners.PlayerClickInvListener;
import pers.soulflame.easymenu.listeners.PlayerOpenInvListener;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.managers.ItemSource;
import pers.soulflame.easymenu.managers.functions.*;
import pers.soulflame.easymenu.managers.sources.BaseSource;
import pers.soulflame.easymenu.managers.sources.NISource;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>插件主类</p>
 */
public final class EasyMenu extends JavaPlugin {

    private static Plugin instance;

    private static Economy economy;

    /**
     * <p>获取vault插件的economy</p>
     *
     * @return economy实例
     */
    public static Economy getEconomy() {
        return economy;
    }

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
        SourceAPI.addSource(source);
    }

    /**
     * <p>添加物品功能</p>
     *
     * @param function 物品功能
     */
    private void addFunction(ItemFunction function) {
        FunctionAPI.addFunction(function);
    }

    @Override
    public void onLoad() {
        instance = this;
        FileUtil.loadAllFiles();
    }

    @Override
    public void onEnable() {

        register(new PlayerCatchListener());
        register(new PlayerClickInvListener());
        register(new PlayerOpenInvListener());

        PluginCommand easymenu = getCommand("easymenu");
        if (easymenu != null) {
            easymenu.setExecutor(new MainCommand());
            easymenu.setTabCompleter(new MainCommand());
        }

        RegisteredServiceProvider<Economy> registration = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (registration != null) economy = registration.getProvider();

        YamlConfiguration config = FileUtil.getConfig();
        ConfigurationSection section = config.getConfigurationSection("sources");
        if (section == null) throw new NullPointerException("Section sources must not be null");
        addSource(new BaseSource(section.getString("easy-menu", "self")));
        final PluginManager manager = Bukkit.getPluginManager();
        final Plugin neigeItems = manager.getPlugin("NeigeItems");
        if (neigeItems != null) addSource(new NISource(section.getString("neige-items", "ni")));

        section = config.getConfigurationSection("functions");
        if (section == null) throw new NullPointerException("Section functions must not be null");
        addFunction(new CatchFunction(section.getString("catch", "catch")));
        addFunction(new CommandFunction(section.getString("command", "command")));
        addFunction(new JSFunction(section.getString("java-script", "js")));
        final Plugin placeholderAPI = manager.getPlugin("PlaceholderAPI");
        if (placeholderAPI != null) addFunction(new PAPIFunction(section.getString("placeholderapi", "papi")));
        final Plugin playerPoints = manager.getPlugin("PlayerPoints");
        if (playerPoints != null) addFunction(new PointsFunction(section.getString("player-points", "points")));
        final Plugin vault = manager.getPlugin("Vault");
        if (vault != null) addFunction(new MoneyFunction(section.getString("vault", "money")));

        TextUtil.startInfo();
    }

    @Override
    public void onDisable() {
        final List<String> list = FileUtil.getLanguage().getStringList("plugin.close");
        final List<String> temp = new ArrayList<>(list.size());
        for (final String line : list) {
            temp.add(line.replace("<author>", getDescription().getAuthors().toString())
                    .replace("<version>", getDescription().getVersion()));
        }
        TextUtil.sendMessage(temp);
    }
}
