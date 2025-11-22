package org.red.minecraft.dellarte.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.red.minecraft.dellarte.library.event.area.entity.AreaPlayerLeashEntityEvent;

public class PlayerLeashEntityEventListener extends AllEventListener<PlayerLeashEntityEvent> {

    public PlayerLeashEntityEventListener() {
        super(AreaPlayerLeashEntityEvent.class, PlayerLeashEntityEvent.class);
        register();
    }

    @Override
    @EventHandler
    public void onEvent(PlayerLeashEntityEvent event) {
        runAreaEvent(event, event.getPlayer().getLocation());
    }
}
