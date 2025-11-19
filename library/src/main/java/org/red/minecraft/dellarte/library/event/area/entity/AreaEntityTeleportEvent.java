package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityTeleportEvent extends AreaEntityEvent<EntityTeleportEvent> {
    public AreaEntityTeleportEvent(Area area, EntityTeleportEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityTeleportEvent.class, k -> new HandlerList());
    }
}
