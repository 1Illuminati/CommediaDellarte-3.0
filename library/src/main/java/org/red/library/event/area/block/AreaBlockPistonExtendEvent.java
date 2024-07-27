
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.red.library.world.Area;

public class AreaBlockPistonExtendEvent extends AreaBlockEvent<BlockPistonExtendEvent> {
    public AreaBlockPistonExtendEvent(Area area, BlockPistonExtendEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockPistonExtendEvent.class, k -> new HandlerList());
    }
}
