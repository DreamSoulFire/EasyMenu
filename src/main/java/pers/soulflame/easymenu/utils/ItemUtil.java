package pers.soulflame.easymenu.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * <p>物品工具类</p>
 */
public final class ItemUtil {

    private ItemUtil() {

    }

    /**
     * <p>判断物品是否为空/空气</p>
     *
     * @param itemStack 需检测的物品
     * @return 是否为空/空气
     */
    public static boolean itemAir(ItemStack itemStack) {
        return itemStack == null || Material.AIR.equals(itemStack.getType()) || itemStack.getAmount() <= 0;
    }

}
