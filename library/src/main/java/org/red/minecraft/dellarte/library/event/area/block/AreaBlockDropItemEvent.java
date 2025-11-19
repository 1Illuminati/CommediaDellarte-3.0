package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDropItemEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockDropItemEvent extends AreaBlockEvent<BlockDropItemEvent> {
    public AreaBlockDropItemEvent(Area area, BlockDropItemEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
