package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.red.library.world.Area;

public class AreaEntityCombustByBlockEvent extends AreaEntityEvent<EntityCombustByBlockEvent> {
    public AreaEntityCombustByBlockEvent(Area area, EntityCombustByBlockEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityCombustByBlockEvent.class, k -> new HandlerList());
    }
}
