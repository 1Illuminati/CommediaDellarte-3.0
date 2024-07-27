package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.red.library.world.Area;

public class AreaSheepDyeWoolEvent extends AreaEntityEvent<SheepDyeWoolEvent> {
    public AreaSheepDyeWoolEvent(Area area, SheepDyeWoolEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaSheepDyeWoolEvent.class, k -> new HandlerList());
    }
}
