package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockCookEvent;
import org.red.library.world.Area;

public class AreaBlockCookEvent extends AreaBlockEvent<BlockCookEvent> {
    public AreaBlockCookEvent(Area area, BlockCookEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
