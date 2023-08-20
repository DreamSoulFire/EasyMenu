package pers.soulflame.easymenu.managers.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.managers.ItemFunction;

public class CommandFunction extends ItemFunction {

    public CommandFunction(String key) {
        super(key);
    }

    /**
     * <p>执行指令功能</p>
     *
     * @param player 玩家
     * @param string 需执行的指令
     * @return 是否成功
     */
    @Override
    protected boolean run(Player player, String string) {
        String[] split = string.split("\n");
        for (String cmd : split) {
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
