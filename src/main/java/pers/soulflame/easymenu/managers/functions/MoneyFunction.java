package pers.soulflame.easymenu.managers.functions;

import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.FileUtil;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.UUID;

public class MoneyFunction extends ItemFunction {

    public MoneyFunction(String key) {
        super(key);
    }

    @Override
    protected boolean run(UUID uuid, String string) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        final Economy economy = (Economy) EasyMenu.getEconomy();
        final double balance = economy.getBalance(player);
        final double need = Double.parseDouble(string);
        final double take = balance - need;
        if (take < 0) {
            String notEnough = FileUtil.getLanguage().getString("plugin.money-not-enough",
                    "&c你的金币不足, 需要 &b<need>&c, 但你只有 &6%vault_eco_balance_fixed%");
            notEnough = PlaceholderAPI.setPlaceholders(player, notEnough).replace("<need>", String.valueOf(need));
            TextUtil.sendMessage(player, notEnough);
            return false;
        }
        economy.withdrawPlayer(player, take);
        return true;
    }
}
