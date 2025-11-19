package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityBreedEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityBreedEvent extends AreaEntityEvent<EntityBreedEvent> {
    public AreaEntityBreedEvent(Area area, EntityBreedEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityBreedEvent.class, k -> new HandlerList());
    }
}
