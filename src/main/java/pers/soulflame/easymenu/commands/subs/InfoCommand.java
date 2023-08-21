package pers.soulflame.easymenu.commands.subs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.api.FunctionAPI;
import pers.soulflame.easymenu.api.SourceAPI;
import pers.soulflame.easymenu.commands.BaseCommand;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.managers.ItemSource;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InfoCommand extends BaseCommand {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        final List<String> info = FileUtil.getLanguage().getStringList("command.info");
        final List<String> temp = new ArrayList<>(info.size());
        final Map<String, ItemSource> sources = SourceAPI.getSources();
        final Map<String, ItemFunction> functions = FunctionAPI.getFunctions();
        for (final String line : info) {
            temp.add(line.replace("<source>", String.valueOf(sources.size()))
                    .replace("<sources>", sources.keySet().toString())
                    .replace("<function>", String.valueOf(functions.size()))
                    .replace("<functions>", functions.keySet().toString()));
        }
        TextUtil.sendMessage(sender, temp);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "em.command.info";
    }

    @Override
    public String getCommandDesc() {
        return "/emenu info";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
