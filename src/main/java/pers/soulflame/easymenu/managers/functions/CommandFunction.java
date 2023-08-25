package pers.soulflame.easymenu.managers.functions;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.UUID;

public class CommandFunction extends ItemFunction {

    public CommandFunction(String key) {
        super(key);
    }

    @Override
    public boolean run(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        final var split = PlaceholderAPI.setPlaceholders(player, TextUtil.color(string)).split("\n");
        for (final var cmd : split) {
            if (cmd.startsWith("[console]: "))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.substring(11));
            else if (cmd.startsWith("[player]: "))
                Bukkit.dispatchCommand(player, cmd.substring(10));
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
