package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityPotionEffectEvent extends AreaEntityEvent<EntityPotionEffectEvent> {
    public AreaEntityPotionEffectEvent(Area area, EntityPotionEffectEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityPotionEffectEvent.class, k -> new HandlerList());
    }
}
