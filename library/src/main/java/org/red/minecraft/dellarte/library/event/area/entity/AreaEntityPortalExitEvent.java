package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityPortalExitEvent extends AreaEntityEvent<EntityPortalExitEvent> {
    public AreaEntityPortalExitEvent(Area area, EntityPortalExitEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityPortalExitEvent.class, k -> new HandlerList());
    }
}
