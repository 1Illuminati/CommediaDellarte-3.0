package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBatToggleSleepEvent extends AreaEntityEvent<BatToggleSleepEvent> {
    public AreaBatToggleSleepEvent(Area area, BatToggleSleepEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBatToggleSleepEvent.class, k -> new HandlerList());
    }
}
