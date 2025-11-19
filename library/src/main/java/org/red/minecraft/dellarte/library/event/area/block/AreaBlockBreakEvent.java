package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockBreakEvent extends AreaBlockEvent<BlockBreakEvent> {
    public AreaBlockBreakEvent(Area area, BlockBreakEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
