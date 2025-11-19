
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockMultiPlaceEvent extends AreaBlockEvent<BlockMultiPlaceEvent> {
    public AreaBlockMultiPlaceEvent(Area area, BlockMultiPlaceEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockMultiPlaceEvent.class, k -> new HandlerList());
    }
}
