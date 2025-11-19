package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityUnleashEvent extends AreaEntityEvent<EntityUnleashEvent> {
    public AreaEntityUnleashEvent(Area area, EntityUnleashEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityUnleashEvent.class, k -> new HandlerList());
    }
}
