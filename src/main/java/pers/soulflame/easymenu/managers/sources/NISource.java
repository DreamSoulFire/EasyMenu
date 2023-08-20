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
        Object value = map.get("value");
        if (value == null) throw new NullPointerException("Value must not be null");
        String id = (String) value;
        if (!ItemManager.INSTANCE.hasItem(id))
            throw new NullPointerException("NeigeItems don't have this item " + id);
        ItemGenerator item = ItemManager.INSTANCE.getItem(id);
        if (item == null) throw new NullPointerException("Item must not be null");
        return item.getStaticItemStack();
    }
}
