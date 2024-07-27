package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.red.library.world.Area;

public class AreaBlockDispenseArmorEvent extends AreaBlockEvent<BlockDispenseArmorEvent> {
    public AreaBlockDispenseArmorEvent(Area area, BlockDispenseArmorEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
