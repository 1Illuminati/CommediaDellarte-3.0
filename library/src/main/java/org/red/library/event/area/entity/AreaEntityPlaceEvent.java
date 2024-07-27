package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.red.library.world.Area;

public class AreaEntityPlaceEvent extends AreaEntityEvent<EntityPlaceEvent> {
    public AreaEntityPlaceEvent(Area area, EntityPlaceEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityPlaceEvent.class, k -> new HandlerList());
    }
}
