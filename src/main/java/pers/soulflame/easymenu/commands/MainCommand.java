package pers.soulflame.easymenu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.soulflame.easymenu.commands.subs.HelpCommand;
import pers.soulflame.easymenu.commands.subs.OpenCommand;
import pers.soulflame.easymenu.commands.subs.ReloadCommand;
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

    private static final Map<String, BaseCommand> commandMap= new HashMap<>();

    public MainCommand() {
        registerCommand(new HelpCommand());
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
        if (args.length == 0) {

            return true;
        }
        final BaseCommand manager = commandMap.get(args[0].toLowerCase());
        if (manager == null) {
            TextUtil.sendMessage(sender, "&c没有这个指令&f: &a" + args[0]);
            return false;
        }
        if (!sender.hasPermission(manager.getPermission())) {
            TextUtil.sendMessage(sender, "&c你没有权限&f: &6" + manager.getPermission());
            return false;
        }
        if (manager.getLength() > args.length) {
            TextUtil.sendMessage("&c指令参数错误或不完整, 请检查是否输错了指令&f: &a" + manager.getCommandDesc());
            return false;
        }
        final String[] strings = Arrays.copyOfRange(args, 1, args.length);
        if (!(sender instanceof Player player)) {
            manager.onConsoleCommand(sender, strings);
            return true;
        }
        manager.onPlayerCommand(player, strings);
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
