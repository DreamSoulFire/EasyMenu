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
import java.util.UUID;

public class PlayerCatchListener implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        final List<UUID> catches = CatchFunction.catches;
        UUID uniqueId = player.getUniqueId();
        if (!catches.contains(uniqueId)) return;
        event.setCancelled(true);
        catches.remove(uniqueId);
        CatchFunction.tempMap.put(uniqueId, message);
        Bukkit.getScheduler().runTask(EasyMenu.getInstance(), () -> MenuIcon.runAfterCatch(uniqueId));
    }

}
