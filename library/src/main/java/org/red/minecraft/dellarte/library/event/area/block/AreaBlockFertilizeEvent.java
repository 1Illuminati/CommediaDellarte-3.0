package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockFertilizeEvent extends AreaBlockEvent<BlockFertilizeEvent> {
    public AreaBlockFertilizeEvent(Area area, BlockFertilizeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockFertilizeEvent.class, k -> new HandlerList());
    }
}
