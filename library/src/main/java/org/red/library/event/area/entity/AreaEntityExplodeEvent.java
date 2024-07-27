package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.red.library.world.Area;

public class AreaEntityExplodeEvent extends AreaEntityEvent<EntityExplodeEvent> {
    public AreaEntityExplodeEvent(Area area, EntityExplodeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityExplodeEvent.class, k -> new HandlerList());
    }
}
