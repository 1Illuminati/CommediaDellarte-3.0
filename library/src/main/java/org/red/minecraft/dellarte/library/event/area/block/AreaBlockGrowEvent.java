
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockGrowEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockGrowEvent extends AreaBlockEvent<BlockGrowEvent> {
    public AreaBlockGrowEvent(Area area, BlockGrowEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockGrowEvent.class, k -> new HandlerList());
    }
}
