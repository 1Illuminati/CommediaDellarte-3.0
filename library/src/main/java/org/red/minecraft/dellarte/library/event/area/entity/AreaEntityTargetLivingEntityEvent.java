package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityTargetLivingEntityEvent extends AreaEntityEvent<EntityTargetLivingEntityEvent> {
    public AreaEntityTargetLivingEntityEvent(Area area, EntityTargetLivingEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityTargetLivingEntityEvent.class, k -> new HandlerList());
    }
}
