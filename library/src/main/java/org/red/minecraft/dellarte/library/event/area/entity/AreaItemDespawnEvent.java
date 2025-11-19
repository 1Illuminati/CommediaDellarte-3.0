package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaItemDespawnEvent extends AreaEntityEvent<ItemDespawnEvent> {
    public AreaItemDespawnEvent(Area area, ItemDespawnEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaItemDespawnEvent.class, k -> new HandlerList());
    }
}
