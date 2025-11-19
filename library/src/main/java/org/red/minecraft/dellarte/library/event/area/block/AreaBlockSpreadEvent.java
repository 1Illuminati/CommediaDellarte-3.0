
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockSpreadEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockSpreadEvent extends AreaBlockEvent<BlockSpreadEvent> {
    public AreaBlockSpreadEvent(Area area, BlockSpreadEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockSpreadEvent.class, k -> new HandlerList());
    }
}
