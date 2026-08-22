package org.red.minecraft.dellarte.event.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.red.minecraft.dellarte.CommediaDellartePlugin;

public class PlayerSessionListener extends DellarteListener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void quitEvent(PlayerQuitEvent event) {
        CommediaDellartePlugin.manager.removeOldPlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void joinEvent(PlayerJoinEvent event) {
        CommediaDellartePlugin.manager.removeOldPlayerData(event.getPlayer().getUniqueId());
    }
}
