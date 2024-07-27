package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.red.library.world.Area;

public class AreaCreeperPowerEvent extends AreaEntityEvent<CreeperPowerEvent> {
    public AreaCreeperPowerEvent(Area area, CreeperPowerEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaCreeperPowerEvent.class, k -> new HandlerList());
    }
}
