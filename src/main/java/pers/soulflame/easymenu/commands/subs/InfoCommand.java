package pers.soulflame.easymenu.commands.subs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.api.FunctionAPI;
import pers.soulflame.easymenu.api.SourceAPI;
import pers.soulflame.easymenu.commands.BaseCommand;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.List;

public class InfoCommand extends BaseCommand {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        final var sources = SourceAPI.getSources();
        final var functions = FunctionAPI.getFunctions();
        EasyLoad.getCommandSec().getStringList("info").stream()
                .map(string -> string.replace("<source>", String.valueOf(sources.size()))
                        .replace("<sources>", sources.keySet().toString())
                        .replace("<function>", String.valueOf(functions.size()))
                        .replace("<functions>", functions.keySet().toString()))
                .forEach(TextUtil::sendMessage);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getCommand() {
        return "info";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
