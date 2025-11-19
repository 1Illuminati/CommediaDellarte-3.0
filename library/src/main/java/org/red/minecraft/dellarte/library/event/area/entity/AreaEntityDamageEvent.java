package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityDamageEvent extends AreaEntityEvent<EntityDamageEvent> {
    public AreaEntityDamageEvent(Area area, EntityDamageEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityDamageEvent.class, k -> new HandlerList());
    }
}
