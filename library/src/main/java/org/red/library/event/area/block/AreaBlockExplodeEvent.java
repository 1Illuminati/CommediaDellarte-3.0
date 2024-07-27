package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExplodeEvent;
import org.red.library.world.Area;

public class AreaBlockExplodeEvent extends AreaBlockEvent<BlockExplodeEvent> {
    public AreaBlockExplodeEvent(Area area, BlockExplodeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockExplodeEvent.class, k -> new HandlerList());
    }
}
