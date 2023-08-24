package pers.soulflame.easymenu.managers.functions;

import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import pers.soulflame.easymenu.EasyLoad;
import pers.soulflame.easymenu.managers.ItemFunction;
import pers.soulflame.easymenu.utils.TextUtil;

import java.util.UUID;

public class MoneyFunction extends ItemFunction {

    public MoneyFunction(String key) {
        super(key);
    }

    @Override
    protected boolean run(UUID uuid, String string) {
        final var player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        final var economy = (Economy) EasyLoad.getEconomy();
        final var balance = economy.getBalance(player);
        final var need = Double.parseDouble(string);
        final var take = balance - need;
        if (take < 0) {
            var notEnough = EasyLoad.getPluginSec().getString("money-not-enough",
                            "<prefix>&c你的金币不足, 需要 &b<need>&c, 但你只有 &6%vault_eco_balance_fixed%")
                    .replace("<need>", String.valueOf(need));
            notEnough = PlaceholderAPI.setPlaceholders(player, notEnough);
            TextUtil.sendMessage(player, notEnough);
            return false;
        }
        economy.withdrawPlayer(player, take);
        return true;
    }
}
