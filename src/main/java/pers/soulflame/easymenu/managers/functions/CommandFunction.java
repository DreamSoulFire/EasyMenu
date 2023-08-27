package pers.soulflame.easymenu.managers.functions;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.UUID;

public class CommandFunction extends ItemFunction {

    public CommandFunction(String key) {
        super(key);
    }

    private void dispatch(CommandSender sender, String line) {
        Bukkit.getScheduler().runTask(EasyMenu.getInstance(), () ->
                Bukkit.dispatchCommand(sender, line));
    }

    @Override
    public boolean run(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        final var split = PlaceholderAPI.setPlaceholders(player, TextUtil.color(string)).split("\n");
        for (final var cmd : split) {
            if (cmd.startsWith("[console]: "))
                dispatch(Bukkit.getConsoleSender(), cmd.substring(11));
            else if (cmd.startsWith("[player]: "))
                dispatch(player, cmd.substring(10));
            else {
                final var error = EasyLoad.getPluginSec().getString("error-command-execute",
                        "<prefix>&c错误的指令行&f: &4<command>").replace("<command>", cmd);
                TextUtil.sendMessage(error);
                return false;
            }
        }
        return true;
    }
}
