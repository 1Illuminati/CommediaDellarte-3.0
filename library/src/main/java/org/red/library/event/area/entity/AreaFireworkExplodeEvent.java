package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.red.library.world.Area;

public class AreaFireworkExplodeEvent extends AreaEntityEvent<FireworkExplodeEvent> {
    public AreaFireworkExplodeEvent(Area area, FireworkExplodeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaFireworkExplodeEvent.class, k -> new HandlerList());
    }
}
