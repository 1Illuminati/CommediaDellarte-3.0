
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.LeavesDecayEvent;
import org.red.library.world.Area;

public class AreaLeavesDecayEvent extends AreaBlockEvent<LeavesDecayEvent> {
    public AreaLeavesDecayEvent(Area area, LeavesDecayEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaLeavesDecayEvent.class, k -> new HandlerList());
    }
}
