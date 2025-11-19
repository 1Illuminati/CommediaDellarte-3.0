package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDamageEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockDamageEvent extends AreaBlockEvent<BlockDamageEvent> {
    public AreaBlockDamageEvent(Area area, BlockDamageEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
