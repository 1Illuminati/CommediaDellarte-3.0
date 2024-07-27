package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExpEvent;
import org.red.library.world.Area;

public class AreaBlockExpEvent extends AreaBlockEvent<BlockExpEvent> {
    public AreaBlockExpEvent(Area area, BlockExpEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
