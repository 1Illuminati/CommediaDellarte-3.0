package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityShootBowEvent extends AreaEntityEvent<EntityShootBowEvent> {
    public AreaEntityShootBowEvent(Area area, EntityShootBowEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityShootBowEvent.class, k -> new HandlerList());
    }
}
