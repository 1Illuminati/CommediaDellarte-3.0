package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.red.library.world.Area;

public class AreaVillagerAcquireTradeEvent extends AreaEntityEvent<VillagerAcquireTradeEvent> {
    public AreaVillagerAcquireTradeEvent(Area area, VillagerAcquireTradeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaVillagerAcquireTradeEvent.class, k -> new HandlerList());
    }
}
