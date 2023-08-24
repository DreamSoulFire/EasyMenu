package pers.soulflame.easymenu.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * <p>信息处理工具类</p>
 */
public final class TextUtil {

    public static String prefix;
    public static String splitLine;

    private TextUtil() {

    }

    /**
     * <p>发送单行插件信息</p>
     *
     * @param message 需发送的信息
     */
    public static void sendMessage(String message) {
        final var sender = Bukkit.getConsoleSender();
        sender.sendMessage(color(message.replace("<prefix>", prefix).replace("<split>", splitLine)));
    }

    /**
     * <p>发送多行插件信息</p>
     *
     * @param messages 需发送的信息
     */
    public static void sendMessage(List<String> messages) {
        for (final var message : messages) sendMessage(message);
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
            message = PlaceholderAPI.setPlaceholders(player, message);
        sender.sendMessage(color(message.replace("<prefix>", prefix).replace("<split>", splitLine)));
    }

    /**
     * <p>向发送者发送解析过字符的集合信息</p>
     *
     * @param sender   发送者
     * @param messages 信息集合
     */
    public static void sendMessage(CommandSender sender, List<String> messages) {
        for (final var message : messages) sendMessage(sender, message);
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
        return texts.stream().map(TextUtil::color).toList();
    }

}
