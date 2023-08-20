package pers.soulflame.easymenu.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.api.MenuAPI;
import pers.soulflame.easymenu.managers.Menu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>文件工具类</p>
 */
public final class FileUtil {

    private FileUtil() {

    }

    public record Result(File file, YamlConfiguration yamlConfiguration) {

    }

    private static final Map<String, Result> fileMap = new HashMap<>();
    private static final Map<String, Menu> menuMap = new HashMap<>();
    private static YamlConfiguration language;

    /**
     * <p>获取当前正在使用的语言文件</p>
     *
     * @return 语言文件的yaml
     */
    public static YamlConfiguration getLanguage() {
        return language;
    }

    /**
     * <p>获取所有文件</p>
     *
     * @return 所有文件的Map
     */
    public static Map<String, Result> getFileMap() {
        return fileMap;
    }

    /**
     * <p>获取所有菜单</p>
     *
     * @return 菜单的Map
     */
    public static Map<String, Menu> getMenuMap() {
        return menuMap;
    }

    /**
     * <p>加载插件所有文件</p>
     */
    public static void loadAllFiles() throws IOException {

        final File folder = EasyMenu.getInstance().getDataFolder();
        FileUtil.createFile(folder, "config.yml");
        final YamlConfiguration config = getFileMap().get("config.yml").yamlConfiguration();

        TextUtil.sendMessage("&a开始加载语言文件夹...");
        String lang = config.getString("language", "zh_cn") + ".yml";
        final Collection<File> languageFiles = getFiles(new File(folder, "languages"), true);
        String langName = "languages/zh_cn.yml";
        if (languageFiles.isEmpty()) {
            FileUtil.createFile(folder, langName);
            languageFiles.add(getFile(langName));
        }
        for (final File file : languageFiles) {
            if (!file.getName().equals(lang)) continue;
            language = YamlConfiguration.loadConfiguration(file);
        }
        TextUtil.sendMessage("&a语言文件夹加载完成, 共加载了 &e" + languageFiles.size() + " 个语言文件, 当前语言 &d" + lang);
        TextUtil.prefix = language.getString("prefix", "&7[&aEasy&6Menu&7] ");

        TextUtil.sendMessage("&a开始加载菜单文件夹...");
        final Collection<File> menuFiles = FileUtil.getFiles(new File(folder, "menus"), true);
        String menuName = "menus/example.yml";
        if (menuFiles.isEmpty()) {
            FileUtil.createFile(folder, menuName);
            menuFiles.add(getFile(menuName));
        }
        for (final File file : menuFiles) {
            final Menu menu = MenuAPI.loadMenu(YamlConfiguration.loadConfiguration(file));
            getMenuMap().put(file.getName(), menu);
        }
        TextUtil.sendMessage("&a菜单文件夹加载完成, 共加载了 &e" + menuFiles.size() + " 个菜单");
    }

    /**
     * <p>创建文件</p>
     *
     * @param folder 文件的上级文件夹
     * @param name   文件名
     */
    public static void createFile(File folder, String name) {
        final File file = new File(folder, name);
        fileMap.put(name, new Result(file, YamlConfiguration.loadConfiguration(file)));
        if (file.exists()) {
            TextUtil.sendMessage("&a默认配置文件 &b" + name + " &a加载成功");
            return;
        }
        TextUtil.sendMessage("&a默认配置文件 &b" + name + " 不存在, 正在生成...");
        EasyMenu.getInstance().saveResource(name, false);
        TextUtil.sendMessage("&a默认配置文件 &b" + name + " 已生成");
    }

    /**
     * <p>从Map缓存中获取文件</p>
     *
     * @param name 文件注册名
     * @return 文件
     */
    public static File getFile(String name) {
        return getFileMap().get(name).file();
    }

    /**
     * <p>通过bukkit的方式保存文件</p>
     *
     * @param yaml bukkit的yaml
     * @param file 文件
     */
    public static void saveFile(YamlConfiguration yaml, File file) throws IOException {
        yaml.save(file);
    }

    /**
     * <p>通过bukkit的方式保存文件</p>
     *
     * @param file 文件
     */
    public static void saveFile(File file) throws IOException {
        final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        saveFile(yaml, file);
    }

    /**
     * <p>递归查找文件夹中的所有文件</p>
     *
     * @param path  需查找的文件夹
     * @param depth 是否深度递归查找
     * @return 文件集合
     */
    public static Collection<File> getFiles(File path, boolean depth) {
        final ArrayList<File> ret = new ArrayList<>();
        if (path == null || !path.exists()) return ret;
        final File[] files = path.listFiles();
        if (files == null) return ret;
        for (final File file : files) {
            if (file.isFile()) {
                ret.add(file);
                continue;
            }
            if (!file.isDirectory() || !depth) continue;
            ret.addAll(getFiles(file, true));
        }
        return ret;
    }
}
