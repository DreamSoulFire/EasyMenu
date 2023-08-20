package pers.soulflame.easymenu.managers.functions;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
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
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        final YamlConfiguration language = FileUtil.getLanguage();
        final PlayerPointsAPI api = PlayerPoints.getInstance().getAPI();
        int have = api.look(uuid);
        final int need = Integer.parseInt(string);
        if (have - need < 0) {
            final String noEnough = language.getString("plugin.points-not-enough", "&c你的点券不足");
            TextUtil.sendMessage(player, noEnough);
            return false;
        }
        have -= need;
        api.set(uuid, have);
        return true;
    }
}
