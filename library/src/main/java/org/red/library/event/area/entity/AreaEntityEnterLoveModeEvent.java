package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.red.library.world.Area;

public class AreaEntityEnterLoveModeEvent extends AreaEntityEvent<EntityEnterLoveModeEvent> {
    public AreaEntityEnterLoveModeEvent(Area area, EntityEnterLoveModeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityEnterLoveModeEvent.class, k -> new HandlerList());
    }
}
