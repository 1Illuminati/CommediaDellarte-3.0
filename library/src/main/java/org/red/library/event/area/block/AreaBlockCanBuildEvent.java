package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.red.library.world.Area;

public class AreaBlockCanBuildEvent extends AreaBlockEvent<BlockCanBuildEvent> {
    public AreaBlockCanBuildEvent(Area area, BlockCanBuildEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockBreakEvent.class, k -> new HandlerList());
    }
}
