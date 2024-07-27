package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.red.library.world.Area;

public class AreaLingeringPotionSplashEvent extends AreaEntityEvent<LingeringPotionSplashEvent> {
    public AreaLingeringPotionSplashEvent(Area area, LingeringPotionSplashEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaLingeringPotionSplashEvent.class, k -> new HandlerList());
    }
}
