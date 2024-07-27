package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.red.library.world.Area;

public class AreaFoodLevelChangeEvent extends AreaEntityEvent<FoodLevelChangeEvent> {
    public AreaFoodLevelChangeEvent(Area area, FoodLevelChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaFoodLevelChangeEvent.class, k -> new HandlerList());
    }
}
