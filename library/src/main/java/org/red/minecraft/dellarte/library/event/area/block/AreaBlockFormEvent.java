package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFormEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaBlockFormEvent extends AreaBlockEvent<BlockFormEvent> {
    public AreaBlockFormEvent(Area area, BlockFormEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockFormEvent.class, k -> new HandlerList());
    }
}
