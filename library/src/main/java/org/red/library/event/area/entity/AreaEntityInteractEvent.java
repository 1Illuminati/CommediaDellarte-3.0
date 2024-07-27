package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityInteractEvent;
import org.red.library.world.Area;

public class AreaEntityInteractEvent extends AreaEntityEvent<EntityInteractEvent> {
    public AreaEntityInteractEvent(Area area, EntityInteractEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityInteractEvent.class, k -> new HandlerList());
    }
}
