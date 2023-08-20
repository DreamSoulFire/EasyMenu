package pers.soulflame.easymenu.managers.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.managers.ItemFunction;

public class CommandFunction extends ItemFunction {

    public CommandFunction(String key) {
        super(key);
    }

    @Override
    protected void run(Player player, String string) {
        String[] split = string.split("\n");
        for (String cmd : split) {
            if (cmd.startsWith("[console]: ")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.substring(11));
            } else if (cmd.startsWith("[player]: ")) {
                Bukkit.dispatchCommand(player, cmd.substring(10));
            }
        }
    }
}
