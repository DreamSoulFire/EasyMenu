package pers.soulflame.easymenu.managers.functions;

import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.UUID;

public class PAPIFunction extends ItemFunction {
    public PAPIFunction(String key) {
        super(key);
    }

    @Override
    protected boolean run(UUID uuid, String string) {
        return ScriptUtil.run(string, uuid);
    }
}
