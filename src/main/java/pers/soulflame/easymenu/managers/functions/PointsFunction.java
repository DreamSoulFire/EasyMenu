package pers.soulflame.easymenu.managers.functions;

import me.clip.placeholderapi.PlaceholderAPI;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.UUID;

public class PointsFunction extends ItemFunction {
    public PointsFunction(String key) {
        super(key);
    }

    /**
     * <p>执行点券扣除功能</p>
     *
     * @param uuid 扣除的玩家
     * @param string 扣除的值
     * @return 是否执行成功
     */
    @Override
    protected boolean run(UUID uuid, String string) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        final PlayerPointsAPI api = PlayerPoints.getInstance().getAPI();
        int have = api.look(uuid);
        final int need = Integer.parseInt(string);
        if (have - need < 0) {
            String notEnough = FileUtil.getLanguage().getString("plugin.points-not-enough",
                    "&c你的点券不足, 需要 &b<need>&c, 但你只有 &6%playerpoints_points%");
            notEnough = PlaceholderAPI.setPlaceholders(player, notEnough).replace("<need>", String.valueOf(need));
            TextUtil.sendMessage(player, notEnough);
            return false;
        }
        have -= need;
        api.set(uuid, have);
        return true;
    }
}
