package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityExhaustionEvent extends AreaEntityEvent<EntityExhaustionEvent> {
    public AreaEntityExhaustionEvent(Area area, EntityExhaustionEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityExhaustionEvent.class, k -> new HandlerList());
    }
}
