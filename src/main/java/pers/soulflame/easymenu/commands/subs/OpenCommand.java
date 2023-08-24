package pers.soulflame.easymenu.commands.subs;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.api.MenuAPI;
import pers.soulflame.easymenu.commands.BaseCommand;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.List;

public class OpenCommand extends BaseCommand {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        final var player = Bukkit.getPlayer(args[0]);
        final var section = EasyLoad.getCommandSec();
        if (section == null) throw new NullPointerException("The section 'command' in language file must not be null");
        if (player == null) {
            final var offline = section.getString("player-offline",
                    "<prefix>&c玩家 &6%player_name% &c不存在或离线")
                    .replace("%player_name%", args[0]);
            TextUtil.sendMessage(sender, offline);
            return;
        }
        if ("true".equalsIgnoreCase(args[2])) {
            final var openOther = section.getString("open-for-other",
                    "<prefix>&a你为 &b%player_name% &a打开了一个菜单");
            TextUtil.sendMessage(sender, PlaceholderAPI.setPlaceholders(player, openOther));
            final var openMenu = section.getString("open-menu", "<prefix>&a你打开了一个菜单");
            TextUtil.sendMessage(player, openMenu);
        }
        final var menu = EasyLoad.getMenus().get(args[1]);
        menu.open(player);
        Bukkit.getScheduler().runTaskAsynchronously(EasyMenu.getInstance(), () -> {
            final var itemMap = MenuAPI.parseInv(menu.layouts(), menu.icons(), player.getUniqueId());
            itemMap.forEach((slot, result) -> menu.getInventory().setItem(slot, result.item()));
        });
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getCommand() {
        return "open";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!"open".equalsIgnoreCase(args[0])) return null;
        return switch (args.length) {
            case 2 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            case 3 -> EasyLoad.getMenus().keySet().stream().toList();
            case 4 -> List.of("true", "false");
            default -> null;
        };
    }
}
