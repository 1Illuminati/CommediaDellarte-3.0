package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityCombustEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityCombustEvent extends AreaEntityEvent<EntityCombustEvent> {
    public AreaEntityCombustEvent(Area area, EntityCombustEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityCombustEvent.class, k -> new HandlerList());
    }
}
