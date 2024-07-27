package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEnterBlockEvent;
import org.red.library.world.Area;

public class AreaEntityEnterBlockEvent extends AreaEntityEvent<EntityEnterBlockEvent> {
    public AreaEntityEnterBlockEvent(Area area, EntityEnterBlockEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityEnterBlockEvent.class, k -> new HandlerList());
    }
}
