package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.red.library.world.Area;

public class AreaEntityPoseChangeEvent extends AreaEntityEvent<EntityPoseChangeEvent> {
    public AreaEntityPoseChangeEvent(Area area, EntityPoseChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityPoseChangeEvent.class, k -> new HandlerList());
    }
}
