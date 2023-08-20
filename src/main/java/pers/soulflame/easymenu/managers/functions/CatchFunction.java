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

    @Override
    protected void run(Player player, String string) {
        TextUtil.sendMessage(player, string);
        catches.add(player);
        player.closeInventory();
    }
}
