
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockIgniteEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockIgniteEvent extends AreaBlockEvent<BlockIgniteEvent> {
    public AreaBlockIgniteEvent(Area area, BlockIgniteEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockIgniteEvent.class, k -> new HandlerList());
    }
}
