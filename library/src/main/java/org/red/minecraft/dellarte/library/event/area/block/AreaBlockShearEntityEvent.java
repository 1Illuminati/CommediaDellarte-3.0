
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockShearEntityEvent extends AreaBlockEvent<BlockShearEntityEvent> {
    public AreaBlockShearEntityEvent(Area area, BlockShearEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockShearEntityEvent.class, k -> new HandlerList());
    }
}
