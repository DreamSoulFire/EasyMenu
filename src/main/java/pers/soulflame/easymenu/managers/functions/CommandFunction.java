package pers.soulflame.easymenu.managers.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.managers.ItemFunction;

import java.util.UUID;

public class CommandFunction extends ItemFunction {

    public CommandFunction(String key) {
        super(key);
    }

    /**
     * <p>执行指令功能</p>
     *
     * @param uuid 玩家
     * @param string 需执行的指令
     * @return 是否成功
     */
    @Override
    protected boolean run(UUID uuid, String string) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        final String[] split = string.split("\n");
        for (final String cmd : split) {
            if (cmd.startsWith("[console]: ")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.substring(11));
            } else if (cmd.startsWith("[player]: ")) {
                Bukkit.dispatchCommand(player, cmd.substring(10));
            } else {
                return false;
            }
        }
        return true;
    }
}
