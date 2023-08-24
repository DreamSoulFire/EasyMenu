package pers.soulflame.easymenu.commands.subs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.commands.BaseCommand;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.List;

public class HelpCommand extends BaseCommand {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        final var help = EasyLoad.getCommandSec().getStringList("help");
        TextUtil.sendMessage(sender, help);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
