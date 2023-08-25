package pers.soulflame.easymenu.managers.conditions;

import org.bukkit.Bukkit;
import pers.soulflame.easymenu.managers.ItemCondition;

import java.util.UUID;

public class PermissionCondition extends ItemCondition {
    public PermissionCondition(String key) {
        super(key);
    }

    @Override
    public boolean check(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        return player.hasPermission(string);
    }
}
