package pers.soulflame.easymenu.commands.subs;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.commands.BaseCommand;
import pers.soulflame.easymenu.menus.Menu;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OpenCommand extends BaseCommand {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            TextUtil.sendMessage(sender, "&c玩家不存在或离线");
            return;
        }
        if ("true".equalsIgnoreCase(args[2]))
            TextUtil.sendMessage(sender, "&a你打开了一个菜单");
        final Map<String, Menu> menus = FileUtil.getMenuMap();
        final Menu menu = menus.get(args[1]);
        menu.open(player);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "em.command.open";
    }

    @Override
    public String getCommandDesc() {
        return "/emenu open 玩家 菜单 是否显示打开信息[true|false]";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!"open".equalsIgnoreCase(args[0])) return null;
        switch (args.length) {
            case 2 -> {
                final Collection<? extends Player> allPlayer = Bukkit.getOnlinePlayers();
                final List<String> players = new ArrayList<>(allPlayer.size());
                for (final Player player : allPlayer) {
                    players.add(player.getName());
                }
                return players;
            }
            case 3 -> {
                return new ArrayList<>(FileUtil.getMenuMap().keySet());
            }
            case 4 -> {
                return List.of("true", "false");
            }
        }
        return null;
    }
}
