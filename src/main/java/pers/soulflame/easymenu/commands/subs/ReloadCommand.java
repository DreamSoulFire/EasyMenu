package pers.soulflame.easymenu.commands.subs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.commands.BaseCommand;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.List;

public class ReloadCommand extends BaseCommand {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        EasyLoad.init();
        final var list = EasyLoad.getCommandSec().getStringList("reload");
        for (final var line : list) {
            if (line.startsWith("<console>")) {
                TextUtil.sendMessage(line.substring(9));
                continue;
            }
            TextUtil.sendMessage(line);
            if (!(sender instanceof final Player player)) continue;
            TextUtil.sendMessage(player, line);
        }
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getCommand() {
        return "reload";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
