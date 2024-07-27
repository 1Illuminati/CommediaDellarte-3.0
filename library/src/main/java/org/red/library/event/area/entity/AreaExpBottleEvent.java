package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ExpBottleEvent;
import org.red.library.world.Area;

public class AreaExpBottleEvent extends AreaEntityEvent<ExpBottleEvent> {
    public AreaExpBottleEvent(Area area, ExpBottleEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaExpBottleEvent.class, k -> new HandlerList());
    }
}
