package pers.soulflame.easymenu.managers.conditions;

import me.clip.placeholderapi.PlaceholderAPI;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.managers.ItemCondition;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.UUID;

public class PointsCondition extends ItemCondition {
    public PointsCondition(String key) {
        super(key);
    }

    @Override
    public boolean check(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        final var api = PlayerPoints.getInstance().getAPI();
        var have = api.look(uuid);
        final var need = Integer.parseInt(string);
        if (have - need < 0) {
            var notEnough = EasyLoad.getPluginSec().getString("points-not-enough",
                            "<prefix>&c你的点券不足, 需要 &b<need>&c, 但你只有 &6%playerpoints_points%")
                    .replace("<need>", String.valueOf(need));
            notEnough = PlaceholderAPI.setPlaceholders(player, notEnough);
            TextUtil.sendMessage(player, notEnough);
            return false;
        }
        have -= need;
        api.set(uuid, have);
        return true;
    }
}
