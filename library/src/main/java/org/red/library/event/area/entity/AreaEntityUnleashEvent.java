package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.red.library.world.Area;

public class AreaEntityUnleashEvent extends AreaEntityEvent<EntityUnleashEvent> {
    public AreaEntityUnleashEvent(Area area, EntityUnleashEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityUnleashEvent.class, k -> new HandlerList());
    }
}
