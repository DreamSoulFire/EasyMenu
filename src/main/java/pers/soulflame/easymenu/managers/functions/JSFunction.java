package pers.soulflame.easymenu.managers.functions;

import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.ScriptUtil;
import pers.soulflame.easymenu.utils.YamlUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class JSFunction extends ItemFunction {
    public JSFunction(String key) {
        super(key);
    }

    @Override
    public boolean run(UUID uuid, String string) {
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
