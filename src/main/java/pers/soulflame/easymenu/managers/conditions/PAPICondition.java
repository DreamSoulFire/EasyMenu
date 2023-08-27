package pers.soulflame.easymenu.managers.conditions;

import pers.soulflame.easymenu.managers.ItemCondition;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.util.UUID;

public class PAPICondition extends ItemCondition {
    public PAPICondition(String key) {
        super(key);
    }

    @Override
    public boolean check(UUID uuid, String string) {
        return ScriptUtil.eval(string, uuid);
    }
}
