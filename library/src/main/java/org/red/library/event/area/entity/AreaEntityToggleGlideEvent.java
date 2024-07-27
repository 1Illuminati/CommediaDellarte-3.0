package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.red.library.world.Area;

public class AreaEntityToggleGlideEvent extends AreaEntityEvent<EntityToggleGlideEvent> {
    public AreaEntityToggleGlideEvent(Area area, EntityToggleGlideEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityToggleGlideEvent.class, k -> new HandlerList());
    }
}
