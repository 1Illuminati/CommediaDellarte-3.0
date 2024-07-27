package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.red.library.world.Area;

public class AreaEntityTeleportEvent extends AreaEntityEvent<EntityTeleportEvent> {
    public AreaEntityTeleportEvent(Area area, EntityTeleportEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityTeleportEvent.class, k -> new HandlerList());
    }
}
