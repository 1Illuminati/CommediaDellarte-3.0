package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityPortalEnterEvent extends AreaEntityEvent<EntityPortalEnterEvent> {
    public AreaEntityPortalEnterEvent(Area area, EntityPortalEnterEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityPortalEnterEvent.class, k -> new HandlerList());
    }
}
