package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTameEvent;
import org.red.library.world.Area;

public class AreaEntityTameEvent extends AreaEntityEvent<EntityTameEvent> {
    public AreaEntityTameEvent(Area area, EntityTameEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityTameEvent.class, k -> new HandlerList());
    }
}
