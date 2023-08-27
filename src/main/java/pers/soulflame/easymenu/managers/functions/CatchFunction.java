package pers.soulflame.easymenu.managers.functions;

import org.bukkit.Bukkit;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.*;

public class CatchFunction extends ItemFunction {


    public static List<UUID> catches = new ArrayList<>();
    public static Map<UUID, String> tempMap = new HashMap<>();

    public CatchFunction(String key) {
        super(key);
    }

    @Override
    public boolean run(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        TextUtil.sendMessage(player, string);
        catches.add(uuid);
        Bukkit.getScheduler().runTask(EasyMenu.getInstance(), player::closeInventory);
        return true;
    }
}
