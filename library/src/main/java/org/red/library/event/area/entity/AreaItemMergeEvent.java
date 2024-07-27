package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemMergeEvent;
import org.red.library.world.Area;

public class AreaItemMergeEvent extends AreaEntityEvent<ItemMergeEvent> {
    public AreaItemMergeEvent(Area area, ItemMergeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaItemMergeEvent.class, k -> new HandlerList());
    }
}
