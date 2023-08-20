package pers.soulflame.easymenu.managers.functions;

import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.UUID;

public class JSFunction extends ItemFunction {
    public JSFunction(String key) {
        super(key);
    }

    /**
     * <p>执行js操作</p>
     *
     * @param uuid 玩家
     * @param string js文本
     * @return 是否执行成功
     */
    @Override
    protected boolean run(UUID uuid, String string) {
        ScriptUtil.eval(string);
        return true;
    }
}
