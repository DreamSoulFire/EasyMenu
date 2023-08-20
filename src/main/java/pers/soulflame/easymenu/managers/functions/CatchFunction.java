package pers.soulflame.easymenu.managers.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.*;

public class CatchFunction extends ItemFunction {

    public static List<UUID> catches = new ArrayList<>();
    public static Map<UUID, String> tempMap = new HashMap<>();

    public CatchFunction(String key) {
        super(key);
    }

    /**
     * <p>执行聊天信息捕获操作</p>
     *
     * @param uuid 玩家
     * @param string 发送的信息
     * @return 是否执行成功
     */
    @Override
    protected boolean run(UUID uuid, String string) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        TextUtil.sendMessage(player, string);
        catches.add(uuid);
        player.closeInventory();
        return true;
    }
}
