package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityDeathEvent extends AreaEntityEvent<EntityDeathEvent> {
    public AreaEntityDeathEvent(Area area, EntityDeathEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityDeathEvent.class, k -> new HandlerList());
    }
}
