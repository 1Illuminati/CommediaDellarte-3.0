package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaItemSpawnEvent extends AreaEntityEvent<ItemSpawnEvent> {
    public AreaItemSpawnEvent(Area area, ItemSpawnEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaItemSpawnEvent.class, k -> new HandlerList());
    }
}
