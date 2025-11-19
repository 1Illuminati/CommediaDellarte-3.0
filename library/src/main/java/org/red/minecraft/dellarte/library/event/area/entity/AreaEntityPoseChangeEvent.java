package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityPoseChangeEvent extends AreaEntityEvent<EntityPoseChangeEvent> {
    public AreaEntityPoseChangeEvent(Area area, EntityPoseChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityPoseChangeEvent.class, k -> new HandlerList());
    }
}
