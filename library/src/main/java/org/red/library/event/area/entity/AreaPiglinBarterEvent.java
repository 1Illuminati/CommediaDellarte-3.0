package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.red.library.world.Area;

public class AreaPiglinBarterEvent extends AreaEntityEvent<PiglinBarterEvent> {
    public AreaPiglinBarterEvent(Area area, PiglinBarterEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPiglinBarterEvent.class, k -> new HandlerList());
    }
}
