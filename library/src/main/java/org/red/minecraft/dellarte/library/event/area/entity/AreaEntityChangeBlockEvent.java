package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityChangeBlockEvent extends AreaEntityEvent<EntityChangeBlockEvent> {
    public AreaEntityChangeBlockEvent(Area area, EntityChangeBlockEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityChangeBlockEvent.class, k -> new HandlerList());
    }
}
