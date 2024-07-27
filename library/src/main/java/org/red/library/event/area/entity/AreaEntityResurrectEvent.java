package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.red.library.world.Area;

public class AreaEntityResurrectEvent extends AreaEntityEvent<EntityResurrectEvent> {
    public AreaEntityResurrectEvent(Area area, EntityResurrectEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityResurrectEvent.class, k -> new HandlerList());
    }
}
