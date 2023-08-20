package pers.soulflame.easymenu.managers.sources;

import org.bukkit.inventory.ItemStack;
import pers.neige.neigeitems.item.ItemGenerator;
import pers.neige.neigeitems.manager.ItemManager;
import pers.soulflame.easymenu.managers.ItemSource;

import java.util.Map;

public class NISource extends ItemSource {

    public NISource(String key) {
        super(key);
    }

    /**
     * 通过NeigeItems获取物品
     *
     * @param map 被解析的map
     * @return ni的物品
     */
    @Override
    protected ItemStack parseItem(Map<String, ?> map) {
        final Object value = map.get("value");
        if (value == null) throw new NullPointerException("Value must not be null");
        final String id = (String) value;
        final ItemGenerator item = ItemManager.INSTANCE.getItem(id);
        if (item == null) throw new NullPointerException("Item must not be null");
        return item.getItemStack(null, "");
    }
}
