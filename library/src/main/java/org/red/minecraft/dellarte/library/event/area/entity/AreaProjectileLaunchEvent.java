package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaProjectileLaunchEvent extends AreaEntityEvent<ProjectileLaunchEvent> {
    public AreaProjectileLaunchEvent(Area area, ProjectileLaunchEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaProjectileLaunchEvent.class, k -> new HandlerList());
    }
}
