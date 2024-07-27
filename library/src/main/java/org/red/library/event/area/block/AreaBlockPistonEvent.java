
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonEvent;
import org.red.library.world.Area;

public class AreaBlockPistonEvent extends AreaBlockEvent<BlockPistonEvent> {
    public AreaBlockPistonEvent(Area area, BlockPistonEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockPistonEvent.class, k -> new HandlerList());
    }
}
