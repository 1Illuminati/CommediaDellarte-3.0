package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.StriderTemperatureChangeEvent;
import org.red.library.world.Area;

public class AreaStriderTemperatureChangeEvent extends AreaEntityEvent<StriderTemperatureChangeEvent> {
    public AreaStriderTemperatureChangeEvent(Area area, StriderTemperatureChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaStriderTemperatureChangeEvent.class, k -> new HandlerList());
    }
}
