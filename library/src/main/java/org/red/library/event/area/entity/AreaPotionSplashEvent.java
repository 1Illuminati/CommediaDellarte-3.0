package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PotionSplashEvent;
import org.red.library.world.Area;

public class AreaPotionSplashEvent extends AreaEntityEvent<PotionSplashEvent> {
    public AreaPotionSplashEvent(Area area, PotionSplashEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPotionSplashEvent.class, k -> new HandlerList());
    }
}
