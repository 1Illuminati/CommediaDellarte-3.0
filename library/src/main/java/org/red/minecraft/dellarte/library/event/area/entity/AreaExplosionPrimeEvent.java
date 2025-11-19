package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaExplosionPrimeEvent extends AreaEntityEvent<ExplosionPrimeEvent> {
    public AreaExplosionPrimeEvent(Area area, ExplosionPrimeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaExplosionPrimeEvent.class, k -> new HandlerList());
    }
}
