package pers.soulflame.easymenu.utils;

import pers.soulflame.easymenu.EasyMenu;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>日志工具类</p>
 */
public final class LogUtil {

    private static final Logger LOGGER;
    private static final String prefix = "&7[&aEasy&6Menu&7] ";

    static {
        LOGGER = EasyMenu.getInstance().getLogger();
    }

    private LogUtil() {

    }

    /**
     * <p>发送日志</p>
     *
     * @param log       需输出的语句
     * @param usePrefix 是否使用插件默认前缀
     */
    public static void log(String log, boolean usePrefix) {
        final Level info = Level.INFO;
        if (usePrefix) {
            LOGGER.log(info, TextUtil.color(prefix + log));
        } else {
            LOGGER.log(info, TextUtil.color(log));
        }
    }

}
