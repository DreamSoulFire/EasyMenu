package pers.soulflame.easymenu.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pers.soulflame.easymenu.managers.MenuIcon;
import pers.soulflame.easymenu.managers.functions.CatchFunction;

public class PlayerCatchListener implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        final var player = event.getPlayer();
        final var message = event.getMessage();
        final var catches = CatchFunction.catches;
        final var uniqueId = player.getUniqueId();
        if (!catches.contains(uniqueId)) return;
        event.setCancelled(true);
        catches.remove(uniqueId);
        CatchFunction.tempMap.put(uniqueId, message);
        MenuIcon.runAfterCatch(uniqueId);
    }

}
