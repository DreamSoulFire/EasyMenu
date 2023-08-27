package pers.soulflame.easymenu.managers.conditions;

import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.managers.ItemCondition;
import pers.soulflame.easymenu.utils.ScriptUtil;
import pers.soulflame.easymenu.utils.YamlUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class JSCondition extends ItemCondition {
    public JSCondition(String key) {
        super(key);
    }

    @Override
    public boolean check(UUID uuid, String string) {
        boolean result = false;
        File file = new File(EasyMenu.getInstance().getDataFolder(), "script/" + string);
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            String script = YamlUtil.loadAs(reader, String.class);
            result = ScriptUtil.eval(script, uuid);
        } catch (IOException ignored) {
        }
        return result;
    }
}
