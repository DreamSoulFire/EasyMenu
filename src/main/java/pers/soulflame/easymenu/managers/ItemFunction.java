package pers.soulflame.easymenu.managers;

import java.util.UUID;

public abstract class ItemFunction {

    private final String key;

    public ItemFunction(String key) {
        this.key = key;
    }

    /**
     * <p>获取物品功能的内部名</p>
     *
     * @return 内部名字符串
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>执行物品功能</p>
     *
     * @param uuid 玩家的uuid
     * @param string 执行的文本
     * @return 是否执行成功
     */
    protected abstract boolean run(UUID uuid, String string);

}
