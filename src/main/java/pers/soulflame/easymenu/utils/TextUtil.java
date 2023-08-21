package pers.soulflame.easymenu.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.api.FunctionAPI;
import pers.soulflame.easymenu.api.SourceAPI;
import pers.soulflame.easymenu.commands.MainCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>信息处理工具类</p>
 */
public final class TextUtil {

    public static String prefix;

    private TextUtil() {

    }

    /**
     * <p>插件启动信息</p>
     */
    public static void startInfo() {
        final List<String> list = FileUtil.getLanguage().getStringList("plugin.start");
        final PluginDescriptionFile description = EasyMenu.getInstance().getDescription();
        final List<String> temp = new ArrayList<>(list.size());
        for (final String line : list) {
            temp.add(line.replace("<author>", description.getAuthors().toString())
                    .replace("<version>", description.getVersion())
                    .replace("<languages>", String.valueOf(FileUtil.getLangFiles().size()))
                    .replace("<lang>", FileUtil.getLang())
                    .replace("<menus>", String.valueOf(FileUtil.getMenuFiles().size()))
                    .replace("<source>", String.valueOf(SourceAPI.getSources().size()))
                    .replace("<sources>", SourceAPI.getSources().keySet().toString())
                    .replace("<function>", String.valueOf(FunctionAPI.getFunctions().size()))
                    .replace("<functions>", FunctionAPI.getFunctions().keySet().toString())
                    .replace("<commands>", String.valueOf(MainCommand.getCommandMap().size()))
            );
        }
        sendMessage(temp);
    }

    /**
     * <p>发送单行插件信息</p>
     *
     * @param message 需发送的信息
     */
    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(color(prefix + message));
    }

    /**
     * <p>发送多行插件信息</p>
     *
     * @param messages 需发送的信息
     */
    public static void sendMessage(List<String> messages) {
        for (final String message : messages) sendMessage(message);
    }

    /**
     * <p>向发送者发送一条</p>
     * <p>解析过颜色字符的单行信息</p>
     *
     * @param sender  发送者
     * @param message 单行信息字符串
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof final Player player)
            message = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, message);
        sender.sendMessage(color(prefix + message));
    }

    /**
     * <p>向发送者发送解析过字符的集合信息</p>
     *
     * @param sender   发送者
     * @param messages 信息集合
     */
    public static void sendMessage(CommandSender sender, List<String> messages) {
        for (final String message : messages)
            sendMessage(sender, message);
    }

    /**
     * <p>解析文本中的颜色字符 '&'</p>
     *
     * @param text 需解析的文本
     * @return 解析后的文本
     */
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * <p>解析文本中的颜色字符 '&'</p>
     *
     * @param texts 需解析的文本
     * @return 解析后的文本
     */
    public static List<String> color(List<String> texts) {
        final List<String> temp = new ArrayList<>(texts.size());
        for (final String text : texts) temp.add(color(text));
        return temp;
    }

}
