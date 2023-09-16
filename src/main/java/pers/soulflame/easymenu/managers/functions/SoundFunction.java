package pers.soulflame.easymenu.managers.functions;

import org.bukkit.Bukkit;
import pers.soulflame.easymenu.managers.ItemFunction;

import java.util.UUID;

public class SoundFunction extends ItemFunction {
    public SoundFunction(String key) {
        super(key);
    }

    @Override
    protected boolean run(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        player.playSound(player, string, 1, 0);
        return true;
    }
}
