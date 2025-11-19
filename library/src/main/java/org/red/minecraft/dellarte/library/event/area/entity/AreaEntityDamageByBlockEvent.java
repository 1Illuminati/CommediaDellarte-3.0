package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityDamageByBlockEvent extends AreaEntityEvent<EntityDamageByBlockEvent> {
    public AreaEntityDamageByBlockEvent(Area area, EntityDamageByBlockEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityDamageByBlockEvent.class, k -> new HandlerList());
    }
}
