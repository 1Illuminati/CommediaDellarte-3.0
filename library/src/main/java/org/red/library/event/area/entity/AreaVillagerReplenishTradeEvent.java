package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.red.library.world.Area;

public class AreaVillagerReplenishTradeEvent extends AreaEntityEvent<VillagerReplenishTradeEvent> {
    public AreaVillagerReplenishTradeEvent(Area area, VillagerReplenishTradeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaVillagerReplenishTradeEvent.class, k -> new HandlerList());
    }
}
