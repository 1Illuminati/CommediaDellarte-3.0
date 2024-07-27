package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.red.library.world.Area;

public class AreaEntityCreatePortalEvent extends AreaEntityEvent<EntityCreatePortalEvent> {
    public AreaEntityCreatePortalEvent(Area area, EntityCreatePortalEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityCreatePortalEvent.class, k -> new HandlerList());
    }
}
