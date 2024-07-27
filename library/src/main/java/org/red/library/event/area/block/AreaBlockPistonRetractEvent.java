
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.red.library.world.Area;

public class AreaBlockPistonRetractEvent extends AreaBlockEvent<BlockPistonRetractEvent> {
    public AreaBlockPistonRetractEvent(Area area, BlockPistonRetractEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockPistonRetractEvent.class, k -> new HandlerList());
    }
}
