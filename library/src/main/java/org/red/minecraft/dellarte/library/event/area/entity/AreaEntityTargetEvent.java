package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTargetEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityTargetEvent extends AreaEntityEvent<EntityTargetEvent> {
    public AreaEntityTargetEvent(Area area, EntityTargetEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityTargetEvent.class, k -> new HandlerList());
    }
}
