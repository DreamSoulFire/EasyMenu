package pers.soulflame.easymenu.managers.conditions;

import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.managers.ItemCondition;
import pers.soulflame.easymenu.managers.functions.CatchFunction;
import pers.soulflame.easymenu.utils.ScriptUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

public class JSCondition extends ItemCondition {
    public JSCondition(String key) {
        super(key);
    }

    @Override
    public boolean check(UUID uuid, String string) {
        File file = new File(EasyMenu.getInstance().getDataFolder(), "scripts/" + string + ".js");
        if (!file.exists()) throw new NullPointerException("File '" + string + "' must not be null");
        try {
            final var catchMsg = CatchFunction.tempMap.get(uuid);
            final var temp = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            final var script = catchMsg == null ? temp :
                    temp.replace("$catch", catchMsg);
            return ScriptUtil.eval(script, uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
