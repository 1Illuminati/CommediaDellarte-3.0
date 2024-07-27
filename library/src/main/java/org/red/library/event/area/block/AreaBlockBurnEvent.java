package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBurnEvent;
import org.red.library.world.Area;

public class AreaBlockBurnEvent extends AreaBlockEvent<BlockBurnEvent> {
    public AreaBlockBurnEvent(Area area, BlockBurnEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
