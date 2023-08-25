package pers.soulflame.easymenu.managers;

import java.util.UUID;

public abstract class ItemCondition {

    private final String key;

    public ItemCondition(String key) {
        this.key = key;
    }

    /**
     * <p>获取物品条件的内部名</p>
     *
     * @return 内部名字符串
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>检测条件是否满足</p>
     *
     * @param uuid   玩家uuid
     * @param string 条件
     * @return 是否满足
     */
    public abstract boolean check(UUID uuid, String string);
}
