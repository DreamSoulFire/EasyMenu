package pers.soulflame.easymenu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.EasyLoad;

import java.util.List;

/**
 * <p>指令基础抽象类</p>
 */
public abstract class BaseCommand {

    /**
     * <p>控制台执行指令</p>
     *
     * @param sender 指令发送者
     * @param args   指令参数
     */
    public abstract void onConsoleCommand(CommandSender sender, String[] args);

    /**
     * <p>玩家执行指令</p>
     *
     * @param player 玩家
     * @param args   指令参数
     */
    public abstract void onPlayerCommand(Player player, String[] args);

    /**
     * <p>获取主指令参数</p>
     *
     * @return 主指令
     */
    public abstract String getCommand();

    /**
     * <p>获取指令所需要的权限</p>
     *
     * @return 权限字符串
     */
    public String getPermission() {
        return EasyLoad.getCommand(getCommand()).permission();
    }

    /**
     * <p>获取指令描述</p>
     *
     * @return 指令描述字符串
     */
    public String getCommandDesc() {
        return EasyLoad.getCommand(getCommand()).args();
    }

    /**
     * <p>判断子类指令描述的长度</p>
     *
     * @return 长度
     */
    public int getLength() {
        return getCommandDesc().split(" ").length - 1;
    }

    /**
     * <p>实现tab补全</p>
     *
     * @param sender  发送者
     * @param command 指令
     * @param label   指令名
     * @param args    所有参数
     * @return 字符串列表
     */
    public abstract List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
}
