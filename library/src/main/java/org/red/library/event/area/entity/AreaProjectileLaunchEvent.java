package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.red.library.world.Area;

public class AreaProjectileLaunchEvent extends AreaEntityEvent<ProjectileLaunchEvent> {
    public AreaProjectileLaunchEvent(Area area, ProjectileLaunchEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaProjectileLaunchEvent.class, k -> new HandlerList());
    }
}
