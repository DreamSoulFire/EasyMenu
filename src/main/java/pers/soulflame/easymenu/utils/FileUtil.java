package pers.soulflame.easymenu.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import pers.soulflame.easymenu.EasyMenu;

import java.io.File;
import java.util.*;

/**
 * <p>文件工具类</p>
 */
public final class FileUtil {

    private FileUtil() {

    }

    public record Result(File file, YamlConfiguration yaml) {

    }

    private static final Map<String, Result> fileMap = new HashMap<>();

    /**
     * <p>获取所有文件的map</p>
     *
     * @return 所有文件的map
     */
    public static Map<String, Result> getFileMap() {
        return fileMap;
    }

    /**
     * <p>创建文件</p>
     *
     * @param folder 文件的上级文件夹
     * @param name   文件名
     */
    public static void createFile(File folder, String name) {
        final var file = new File(folder, name);
        if (!file.exists()) EasyMenu.getInstance().saveResource(name, false);
        final var yaml = YamlConfiguration.loadConfiguration(file);
        final var value = new Result(file, yaml);
        fileMap.put(name, value);
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
     * <p>从Map缓存中获取文件</p>
     *
     * @param name 文件注册名
     * @return 文件
     */
    public static YamlConfiguration getYaml(String name) {
        return getFileMap().get(name).yaml();
    }

    /**
     * <p>递归查找文件夹中的所有文件</p>
     *
     * @param path  需查找的文件夹
     * @param depth 是否深度递归查找
     * @return 文件集合
     */
    public static Collection<File> getFiles(File path, boolean depth) {
        final List<File> ret = new ArrayList<>();
        if (path == null || !path.exists()) return ret;
        final var files = path.listFiles();
        if (files == null) return ret;
        for (final var file : files) {
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
