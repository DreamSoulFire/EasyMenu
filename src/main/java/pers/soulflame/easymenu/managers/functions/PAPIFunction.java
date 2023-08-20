package pers.soulflame.easymenu.managers.functions;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.UUID;

public class PAPIFunction extends ItemFunction {
    public PAPIFunction(String key) {
        super(key);
    }

    @Override
    protected boolean run(UUID uuid, String string) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        string = PlaceholderAPI.setPlaceholders(player, string);
        Object eval = ScriptUtil.eval(string);
        if (!(eval instanceof Boolean result)) return false;
        return result;
    }
}
