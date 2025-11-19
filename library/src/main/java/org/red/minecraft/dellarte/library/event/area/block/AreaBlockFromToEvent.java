package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFromToEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockFromToEvent extends AreaBlockEvent<BlockFromToEvent> {
    public AreaBlockFromToEvent(Area area, BlockFromToEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockFromToEvent.class, k -> new HandlerList());
    }
}
