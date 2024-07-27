package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PigZapEvent;
import org.red.library.world.Area;

public class AreaPigZapEvent extends AreaEntityEvent<PigZapEvent> {
    public AreaPigZapEvent(Area area, PigZapEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPigZapEvent.class, k -> new HandlerList());
    }
}
