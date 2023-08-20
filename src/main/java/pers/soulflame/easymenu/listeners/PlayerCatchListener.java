package pers.soulflame.easymenu.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pers.soulflame.easymenu.EasyMenu;
import pers.soulflame.easymenu.managers.MenuIcon;
import pers.soulflame.easymenu.managers.functions.CatchFunction;

import java.util.List;

public class PlayerCatchListener implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        List<Player> catches = CatchFunction.catches;
        if (!catches.contains(player)) return;
        event.setCancelled(true);
        catches.remove(player);
        CatchFunction.tempMap.put(player, message);
        Bukkit.getScheduler().runTask(EasyMenu.getInstance(), () -> MenuIcon.runAfterCatch(player));
    }

}
