package pers.soulflame.easymenu.managers.functions;

import org.bukkit.entity.Player;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatchFunction extends ItemFunction {

    public static List<Player> catches = new ArrayList<>();
    public static Map<Player, String> tempMap = new HashMap<>();

    public CatchFunction(String key) {
        super(key);
    }

    /**
     * <p>执行聊天信息捕获操作</p>
     *
     * @param player 玩家
     * @param string 发送的信息
     * @return 是否执行成功
     */
    @Override
    protected boolean run(Player player, String string) {
        TextUtil.sendMessage(player, string);
        catches.add(player);
        player.closeInventory();
        return true;
    }
}
