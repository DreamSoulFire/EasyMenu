package pers.soulflame.easymenu;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pers.soulflame.easymenu.api.FunctionAPI;
import pers.soulflame.easymenu.api.MenuAPI;
import pers.soulflame.easymenu.api.SourceAPI;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.managers.ItemSource;
import pers.soulflame.easymenu.managers.Menu;
import pers.soulflame.easymenu.managers.functions.*;
import pers.soulflame.easymenu.managers.sources.BaseSource;
import pers.soulflame.easymenu.managers.sources.NISource;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.ScriptUtil;
import pers.soulflame.easymenu.utils.TextUtil;
import pers.soulflame.easymenu.utils.YamlUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>插件加载工具</p>
 */
public final class EasyLoad {

    private EasyLoad() {

    }

    private static FileConfiguration config;
    private static Object economy;

    /**
     * <p>获取vault插件的economy</p>
     *
     * @return economy实例
     */
    public static Object getEconomy() {
        return economy;
    }

    /**
     * <p>重载插件</p>
     */
    public static void init() {
        config();
        languages();
        commands();
        final var description = EasyMenu.getInstance().getDescription();
        pluginSec.getStringList("info").stream()
                .map(string -> string.replace("<author>", description.getAuthors().toString())
                        .replace("<version>", description.getVersion()))
                .forEach(TextUtil::sendMessage);
        TextUtil.sendMessage(pluginSec.getStringList("languages.start"));
        pluginSec.getStringList("languages.finish").stream().map(string -> string
                .replace("<amount>", String.valueOf(langFiles.size()))
                .replace("<lang>", lang)).forEach(TextUtil::sendMessage);
        menus();
        sources();
        functions();
    }

    private static final File folder = EasyMenu.getInstance().getDataFolder();
    private static String lang;

    /**
     * <p>加载配置文件</p>
     */
    public static void config() {
        FileUtil.createFile(folder, "config.yml");
        config = FileUtil.getYaml("config.yml");
        final var scripts = config.getConfigurationSection("script-tools");
        if (scripts != null)
            for (final var key : scripts.getKeys(false)) {
                final var clazz = scripts.getString(key, "");
                try {
                    ScriptUtil.getEngine().put(key, Class.forName(clazz));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        lang = config.getString("language", "zh_cn");
    }

    private static Collection<File> langFiles;
    private static YamlConfiguration language;

    private static ConfigurationSection pluginSec;
    private static ConfigurationSection commandSec;

    /**
     * <p>获取插件信息节点</p>
     *
     * @return 节点
     */
    public static ConfigurationSection getPluginSec() {
        return pluginSec;
    }

    /**
     * <p>获取指令信息节点</p>
     *
     * @return 节点
     */
    public static ConfigurationSection getCommandSec() {
        return commandSec;
    }

    /**
     * <p>获取当前语言文件</p>
     *
     * @return 语言文件
     */
    public static YamlConfiguration getLanguage() {
        return language;
    }

    /**
     * <p>设置当前语言文件</p>
     *
     * @param language 语言文件配置
     */
    public static void setLanguage(YamlConfiguration language) {
        EasyLoad.language = language;
    }

    /**
     * <p>加载语言文件</p>
     */
    public static void languages() {
        langFiles = FileUtil.getFiles(new File(folder, "languages"), true);
        final var langName = "languages/zh_cn.yml";
        if (langFiles.isEmpty()) {
            FileUtil.createFile(folder, langName);
            langFiles.add(FileUtil.getFile(langName));
        }
        final var file = new File(folder, "languages/" + lang + ".yml");
        if (!langFiles.contains(file)) {
            getLanguage().getStringList("plugin.cast-language-to-chinese").stream().map(s ->
                    s.replace("<lang>", lang)).forEach(TextUtil::sendMessage);
            lang = "zh_cn";
            final var normal = new File(folder, "languages/zh_cn.yml");
            setLanguage(YamlConfiguration.loadConfiguration(normal));
        } else setLanguage(YamlConfiguration.loadConfiguration(file));
        TextUtil.prefix = getLanguage().getString("prefix", "&7[&aEasy&6Menu&7] ");
        TextUtil.splitLine = getLanguage().getString("split-line",
                "&6&m                                                                ");
        pluginSec = getLanguage().getConfigurationSection("plugin");
        if (pluginSec == null) throw new NullPointerException("Section 'plugin' must not be null");
        commandSec = getLanguage().getConfigurationSection("command");
        if (pluginSec == null) throw new NullPointerException("Section 'command' must not be null");
    }

    private static final Map<String, Command> commandMap = new HashMap<>();

    /**
     * <p>获取指令map</p>
     *
     * @return map
     */
    public static Map<String, Command> getCommandMap() {
        return commandMap;
    }

    public static Command getCommand(String command) {
        return commandMap.get(command);
    }

    public record Command(String args, String permission, String notice) {

    }

    @SuppressWarnings("unchecked")
    public static void commands() {
        File file = new File(folder, "commands.yml");
        FileUtil.createFile(folder, "commands.yml");
        try (final var reader = new FileReader(file, StandardCharsets.UTF_8)) {
            final var commandList = (Map<String, Object>) YamlUtil.load(reader);
            if (commandList == null) return;
            for (final var entry : commandList.entrySet()) {
                final var name = entry.getKey();
                final var map = (Map<String, Object>) entry.getValue();
                final var args = (String) map.get("args");
                final var permission = (String) map.get("permission");
                final var notice = (String) map.get("notice");
                commandMap.put(name, new Command(args, permission, notice));
            }
        } catch (IOException ignored) {
        }
    }

    private static final Map<String, Menu> menus = new HashMap<>();

    /**
     * <p>获取所有菜单对象</p>
     *
     * @return 菜单对象的map
     */
    public static Map<String, Menu> getMenus() {
        return menus;
    }

    /**
     * <p>加载菜单文件夹</p>
     */
    public static void menus() {
        TextUtil.sendMessage(getPluginSec().getStringList("menus.start"));
        final var menuName = "menus/example.yml";
        Collection<File> menuFiles = FileUtil.getFiles(new File(folder, "menus"), true);
        if (menuFiles.isEmpty()) {
            FileUtil.createFile(folder, menuName);
            menuFiles.add(FileUtil.getFile(menuName));
        }
        menus.putAll(menuFiles.stream().collect(Collectors.toMap(File::getName,
                file -> MenuAPI.loadMenu(file.getPath()))));
        getPluginSec().getStringList("menus.finish").stream().map(string ->
                string.replace("<amount>", String.valueOf(menus.size()))).forEach(TextUtil::sendMessage);
    }

    public static boolean hasPlugin(String plugin) {
        return Bukkit.getPluginManager().getPlugin(plugin) != null;
    }

    /**
     * <p>添加物品源</p>
     *
     * @param source 物品源
     */
    public static void addSource(ItemSource source) {
        SourceAPI.addSource(source);
    }

    /**
     * <p>加载物品源</p>
     */
    public static void sources() {
        TextUtil.sendMessage(getPluginSec().getStringList("sources.start"));
        SourceAPI.getSources().clear();
        final var section = config.getConfigurationSection("sources");
        if (section == null) throw new NullPointerException("Section 'sources' must not be null");
        addSource(new BaseSource(section.getString("easy-menu", "self")));
        if (hasPlugin("NeigeItems")) addSource(new NISource(section.getString("neige-items", "ni")));
        getPluginSec().getStringList("sources.finish").stream()
                .map(string -> string.replace("<amount>",
                        String.valueOf(SourceAPI.getSources().size())))
                .forEach(TextUtil::sendMessage);
    }

    /**
     * <p>添加物品功能</p>
     *
     * @param function 物品功能
     */
    public static void addFunction(ItemFunction function) {
        FunctionAPI.addFunction(function);
    }

    /**
     * <p>加载物品功能</p>
     */
    public static void functions() {
        TextUtil.sendMessage(getPluginSec().getStringList("functions.start"));
        FunctionAPI.getFunctions().clear();
        final var section = config.getConfigurationSection("functions");
        if (section == null) throw new NullPointerException("Section 'functions' must not be null");
        addFunction(new CatchFunction(section.getString("catch", "catch")));
        addFunction(new CommandFunction(section.getString("command", "command")));
        addFunction(new JSFunction(section.getString("java-script", "js")));
        addFunction(new PermissionFunction(section.getString("permission", "perm")));
        if (hasPlugin("Vault")) {
            final var registration = Bukkit.getServicesManager().getRegistration(Economy.class);
            if (registration != null) economy = registration.getProvider();
            addFunction(new MoneyFunction(section.getString("vault", "money")));
        }
        if (hasPlugin("PlaceholderAPI"))
            addFunction(new PAPIFunction(section.getString("placeholderapi", "papi")));
        if (hasPlugin("PlayerPoints"))
            addFunction(new PointsFunction(section.getString("player-points", "points")));
        getPluginSec().getStringList("functions.finish").stream()
                .map(string -> string.replace("<amount>",
                        String.valueOf(FunctionAPI.getFunctions().size())))
                .forEach(TextUtil::sendMessage);
    }
}
