package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityResurrectEvent extends AreaEntityEvent<EntityResurrectEvent> {
    public AreaEntityResurrectEvent(Area area, EntityResurrectEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityResurrectEvent.class, k -> new HandlerList());
    }
}
