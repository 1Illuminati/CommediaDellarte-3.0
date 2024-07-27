package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.red.library.world.Area;

public class AreaEntityDeathEvent extends AreaEntityEvent<EntityDeathEvent> {
    public AreaEntityDeathEvent(Area area, EntityDeathEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityDeathEvent.class, k -> new HandlerList());
    }
}
