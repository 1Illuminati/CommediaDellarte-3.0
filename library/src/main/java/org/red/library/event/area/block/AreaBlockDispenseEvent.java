package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDispenseEvent;
import org.red.library.world.Area;

public class AreaBlockDispenseEvent extends AreaBlockEvent<BlockDispenseEvent> {
    public AreaBlockDispenseEvent(Area area, BlockDispenseEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
