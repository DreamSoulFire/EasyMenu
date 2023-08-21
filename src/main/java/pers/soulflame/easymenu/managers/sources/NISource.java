package pers.soulflame.easymenu.managers.sources;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.neige.neigeitems.item.ItemGenerator;
import pers.neige.neigeitems.manager.ItemManager;
import pers.soulflame.easymenu.managers.ItemSource;

import java.util.Map;
import java.util.UUID;

public class NISource extends ItemSource {

    public NISource(String key) {
        super(key);
    }

    /**
     * 通过NeigeItems获取物品
     *
     * @param uuid 解析变量的玩家
     * @param map  被解析的map
     * @return ni的物品
     */
    @Override
    protected ItemStack parseItem(UUID uuid, Map<String, ?> map) {
        Player player = null;
        if (uuid != null) player = Bukkit.getPlayer(uuid);
        final Object value = map.get("value");
        if (value == null) throw new NullPointerException("Value must not be null");
        final String id = (String) value;
        final ItemGenerator item = ItemManager.INSTANCE.getItem(id);
        if (item == null) throw new NullPointerException("Item must not be null");
        return uuid == null ? item.getItemStack(null, "") :
                item.getItemStack(player, "");
    }
}
