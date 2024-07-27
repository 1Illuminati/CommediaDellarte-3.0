package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.red.library.world.Area;

public class AreaEntityBreakDoorEvent extends AreaEntityEvent<EntityBreakDoorEvent> {
    public AreaEntityBreakDoorEvent(Area area, EntityBreakDoorEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityBreakDoorEvent.class, k -> new HandlerList());
    }
}
