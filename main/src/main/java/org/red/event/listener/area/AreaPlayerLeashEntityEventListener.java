package org.red.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.red.library.event.area.entity.AreaPlayerLeashEntityEvent;

public class AreaPlayerLeashEntityEventListener extends AreaEventListener<PlayerLeashEntityEvent> {

    public AreaPlayerLeashEntityEventListener() {
        super(AreaPlayerLeashEntityEvent.class, PlayerLeashEntityEvent.class);
        register();
    }

    @Override
    @EventHandler
    public void onEvent(PlayerLeashEntityEvent event) {
        runAreaEvent(event, event.getPlayer().getLocation());
    }
}
