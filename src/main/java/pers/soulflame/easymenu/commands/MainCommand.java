package pers.soulflame.easymenu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.soulflame.easymenu.commands.subs.HelpCommand;
import pers.soulflame.easymenu.commands.subs.InfoCommand;
import pers.soulflame.easymenu.commands.subs.OpenCommand;
import pers.soulflame.easymenu.commands.subs.ReloadCommand;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>命令主类</p>
 */
public class MainCommand implements TabExecutor {

    private static final Map<String, BaseCommand> commandMap = new HashMap<>();

    public MainCommand() {
        registerCommand(new HelpCommand());
        registerCommand(new InfoCommand());
        registerCommand(new OpenCommand());
        registerCommand(new ReloadCommand());
    }

    public static Map<String, BaseCommand> getCommandMap() {
        return commandMap;
    }

    /**
     * 注册该指令
     *
     * @param manager 需注册的指令
     */
    private void registerCommand(BaseCommand manager) {
        commandMap.put(manager.getCommand(), manager);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        YamlConfiguration language = FileUtil.getLanguage();
        if (args.length == 0) {
            final List<String> help = language.getStringList("command.help");
            TextUtil.sendMessage(sender, help);
            return true;
        }
        final BaseCommand manager = commandMap.get(args[0].toLowerCase());
        if (manager == null) {
            final String noCommand = language.getString("command.no-such-command", "&c没有这个指令&f: &a<command>")
                    .replace("<command>", args[0]);
            TextUtil.sendMessage(sender, noCommand);
            return false;
        }
        if (!sender.hasPermission(manager.getPermission())) {
            final String noPermission = language.getString("command.no-permission", "&c你没有权限&f: &6<permission>")
                    .replace("<permission>", manager.getPermission());
            TextUtil.sendMessage(sender, noPermission);
            return false;
        }
        if (manager.getLength() > args.length) {
            final String noArgs = language.getString("args-not-enough", "&c指令参数错误或不完整, 请检查是否输错了指令&f: &a<desc>")
                    .replace("<desc>", manager.getCommandDesc());
            TextUtil.sendMessage(sender, noArgs);
            return false;
        }
        final String[] strings = Arrays.copyOfRange(args, 1, args.length);
        if (!(sender instanceof Player)) {
            manager.onConsoleCommand(sender, strings);
            return true;
        }
        manager.onPlayerCommand((Player) sender, strings);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1)
            return commandMap.keySet().stream().filter(cmd -> cmd.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        else if (args.length >= 2)
            return commandMap.containsKey(args[0].toLowerCase()) ? commandMap.get(args[0].toLowerCase()).
                    onTabComplete(sender, command, label, args) : null;
        return null;
    }
}
