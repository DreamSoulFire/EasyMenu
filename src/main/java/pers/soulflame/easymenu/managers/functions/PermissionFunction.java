package pers.soulflame.easymenu.managers.functions;

import org.bukkit.Bukkit;
import pers.soulflame.easymenu.managers.ItemFunction;

import java.util.UUID;

public class PermissionFunction extends ItemFunction {
    public PermissionFunction(String key) {
        super(key);
    }

    @Override
    protected boolean run(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        return player.hasPermission(string);
    }
}
