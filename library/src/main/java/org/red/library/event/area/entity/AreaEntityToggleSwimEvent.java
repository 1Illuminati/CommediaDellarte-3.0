package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.red.library.world.Area;

public class AreaEntityToggleSwimEvent extends AreaEntityEvent<EntityToggleSwimEvent> {
    public AreaEntityToggleSwimEvent(Area area, EntityToggleSwimEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityToggleSwimEvent.class, k -> new HandlerList());
    }
}
