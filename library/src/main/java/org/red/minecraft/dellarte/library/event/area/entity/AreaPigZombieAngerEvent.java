package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PigZombieAngerEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPigZombieAngerEvent extends AreaEntityEvent<PigZombieAngerEvent> {
    public AreaPigZombieAngerEvent(Area area, PigZombieAngerEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPigZombieAngerEvent.class, k -> new HandlerList());
    }
}
