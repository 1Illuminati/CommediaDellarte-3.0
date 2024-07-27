package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.red.library.world.Area;

public class AreaSheepRegrowWoolEvent extends AreaEntityEvent<SheepRegrowWoolEvent> {
    public AreaSheepRegrowWoolEvent(Area area, SheepRegrowWoolEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaSheepRegrowWoolEvent.class, k -> new HandlerList());
    }
}
