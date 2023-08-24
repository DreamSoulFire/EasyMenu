package pers.soulflame.easymenu.managers.functions;

import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.UUID;

public class JSFunction extends ItemFunction {
    public JSFunction(String key) {
        super(key);
    }

    @Override
    protected boolean run(UUID uuid, String string) {
        return ScriptUtil.run(string, uuid);
    }
}
