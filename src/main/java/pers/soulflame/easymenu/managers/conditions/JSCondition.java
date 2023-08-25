package pers.soulflame.easymenu.managers.conditions;

import pers.soulflame.easymenu.managers.ItemCondition;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.UUID;

public class JSCondition extends ItemCondition {
    public JSCondition(String key) {
        super(key);
    }

    @Override
    public boolean check(UUID uuid, String string) {
        return ScriptUtil.run(string, uuid);
    }
}
