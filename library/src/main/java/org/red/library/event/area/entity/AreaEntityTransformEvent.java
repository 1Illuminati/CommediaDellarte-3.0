package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTransformEvent;
import org.red.library.world.Area;

public class AreaEntityTransformEvent extends AreaEntityEvent<EntityTransformEvent> {
    public AreaEntityTransformEvent(Area area, EntityTransformEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityTransformEvent.class, k -> new HandlerList());
    }
}
