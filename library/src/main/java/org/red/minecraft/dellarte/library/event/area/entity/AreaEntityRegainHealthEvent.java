package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityRegainHealthEvent extends AreaEntityEvent<EntityRegainHealthEvent> {
    public AreaEntityRegainHealthEvent(Area area, EntityRegainHealthEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityRegainHealthEvent.class, k -> new HandlerList());
    }
}
