package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.red.library.world.Area;

public class AreaEntityCombustByEntityEvent extends AreaEntityEvent<EntityCombustByEntityEvent> {
    public AreaEntityCombustByEntityEvent(Area area, EntityCombustByEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityCombustByEntityEvent.class, k -> new HandlerList());
    }
}
