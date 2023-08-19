package pers.soulflame.easymenu.commands.subs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.commands.BaseCommand;

import java.util.List;

public class HelpCommand extends BaseCommand {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {

    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "em.command.help";
    }

    @Override
    public String getCommandDesc() {
        return "/emenu help";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
