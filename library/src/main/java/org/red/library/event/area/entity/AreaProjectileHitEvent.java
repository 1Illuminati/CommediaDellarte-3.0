package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.red.library.world.Area;

public class AreaProjectileHitEvent extends AreaEntityEvent<ProjectileHitEvent> {
    public AreaProjectileHitEvent(Area area, ProjectileHitEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaProjectileHitEvent.class, k -> new HandlerList());
    }
}
