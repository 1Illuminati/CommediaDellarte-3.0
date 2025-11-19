package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityPickupItemEvent extends AreaEntityEvent<EntityPickupItemEvent> {
    public AreaEntityPickupItemEvent(Area area, EntityPickupItemEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityPickupItemEvent.class, k -> new HandlerList());
    }
}
