package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.red.library.world.Area;

public class AreaEntityDropItemEvent extends AreaEntityEvent<EntityDropItemEvent> {
    public AreaEntityDropItemEvent(Area area, EntityDropItemEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityDropItemEvent.class, k -> new HandlerList());
    }
}
