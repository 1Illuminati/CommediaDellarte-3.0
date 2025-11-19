package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFadeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockFadeEvent extends AreaBlockEvent<BlockFadeEvent> {
    public AreaBlockFadeEvent(Area area, BlockFadeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
