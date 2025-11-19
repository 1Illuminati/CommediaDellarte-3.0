package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaArrowBodyCountChangeEvent extends AreaEntityEvent<ArrowBodyCountChangeEvent> {
    public AreaArrowBodyCountChangeEvent(Area area, ArrowBodyCountChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaArrowBodyCountChangeEvent.class, k -> new HandlerList());
    }
}
