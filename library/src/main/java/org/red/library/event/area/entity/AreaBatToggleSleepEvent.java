package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.red.library.world.Area;

public class AreaBatToggleSleepEvent extends AreaEntityEvent<BatToggleSleepEvent> {
    public AreaBatToggleSleepEvent(Area area, BatToggleSleepEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBatToggleSleepEvent.class, k -> new HandlerList());
    }
}
