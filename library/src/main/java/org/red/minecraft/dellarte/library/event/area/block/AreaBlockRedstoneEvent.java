
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockRedstoneEvent extends AreaBlockEvent<BlockRedstoneEvent> {
    public AreaBlockRedstoneEvent(Area area, BlockRedstoneEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockRedstoneEvent.class, k -> new HandlerList());
    }
}
