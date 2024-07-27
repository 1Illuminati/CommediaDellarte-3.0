package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPortalEvent;
import org.red.library.world.Area;

public class AreaEntityPortalEvent extends AreaEntityEvent<EntityPortalEvent> {
    public AreaEntityPortalEvent(Area area, EntityPortalEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityPortalEvent.class, k -> new HandlerList());
    }
}
