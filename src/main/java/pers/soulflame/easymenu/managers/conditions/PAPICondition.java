package pers.soulflame.easymenu.managers.conditions;

import org.bukkit.Bukkit;
import pers.soulflame.easymenu.managers.ItemCondition;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.UUID;

public class PAPICondition extends ItemCondition {
    public PAPICondition(String key) {
        super(key);
    }

    @Override
    public boolean check(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        return ScriptUtil.eval(string, player);
    }
}
