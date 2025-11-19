package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTameEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityTameEvent extends AreaEntityEvent<EntityTameEvent> {
    public AreaEntityTameEvent(Area area, EntityTameEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityTameEvent.class, k -> new HandlerList());
    }
}
