package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.red.library.event.area.AreaEvent;
import org.red.library.world.Area;

public class AreaPlayerLeashEntityEvent extends AreaEvent<PlayerLeashEntityEvent> {
    public AreaPlayerLeashEntityEvent(Area area, PlayerLeashEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerLeashEntityEvent.class, k -> new HandlerList());
    }
}
