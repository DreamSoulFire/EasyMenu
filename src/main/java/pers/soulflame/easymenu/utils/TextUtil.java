package pers.soulflame.easymenu.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
     * <p>向发送者发送一条</p>
     * <p>解析过颜色字符的单行信息</p>
     *
     * @param sender  发送者
     * @param message 单行信息字符串
     */
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(prefix + message));
    }

    /**
     * <p>向发送者发送解析过字符的集合信息</p>
     *
     * @param sender   发送者
     * @param messages 信息集合
     */
    public static void sendMessage(CommandSender sender, List<String> messages) {
        for (String message : messages)
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
        final List<String> temp = new ArrayList<>();
        for (final String text : texts) {
            temp.add(color(text));
        }
        return temp;
    }

}
