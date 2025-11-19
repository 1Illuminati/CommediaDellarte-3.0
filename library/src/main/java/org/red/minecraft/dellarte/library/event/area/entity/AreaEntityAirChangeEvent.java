package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityAirChangeEvent extends AreaEntityEvent<EntityAirChangeEvent> {
    public AreaEntityAirChangeEvent(Area area, EntityAirChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityAirChangeEvent.class, k -> new HandlerList());
    }
}
