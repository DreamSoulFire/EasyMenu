package pers.soulflame.easymenu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.commands.subs.*;
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
        registerCommand(new CloseCommand());
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
        var section = EasyLoad.getCommandSec();
        if (args.length == 0) {
            final var header = section.getStringList("help.header");
            TextUtil.sendMessage(sender, header);
            final var commands = section.getStringList("help.commands");
            EasyLoad.getCommandMap().values().forEach(cmd -> commands.stream()
                    .map(s -> s.replace("<args>", cmd.args())
                            .replace("<notice>", cmd.notice()))
                    .forEach(s -> TextUtil.sendMessage(sender, s)));
            final var footer = section.getStringList("help.footer");
            TextUtil.sendMessage(sender, footer);
            return true;
        }
        final var manager = commandMap.get(args[0].toLowerCase());
        if (manager == null) {
            final var noCommand = section.getString("no-such-command", "<prefix>&c没有这个指令&f: &a<command>")
                    .replace("<command>", args[0]);
            TextUtil.sendMessage(sender, noCommand);
            return false;
        }
        if (!sender.hasPermission(manager.getPermission())) {
            final var noPermission = section.getString("no-permission", "<prefix>&c你没有权限&f: &6<permission>")
                    .replace("<permission>", manager.getPermission());
            TextUtil.sendMessage(sender, noPermission);
            return false;
        }
        if (manager.getLength() > args.length) {
            section.getStringList("args-not-enough").stream()
                    .map(string -> string.replace("<desc>", manager.getCommandDesc()))
                    .forEach(TextUtil::sendMessage);
            return false;
        }
        final var strings = Arrays.copyOfRange(args, 1, args.length);
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
