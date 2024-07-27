
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.red.library.world.Area;

public class AreaBlockPhysicsEvent extends AreaBlockEvent<BlockPhysicsEvent> {
    public AreaBlockPhysicsEvent(Area area, BlockPhysicsEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockPhysicsEvent.class, k -> new HandlerList());
    }
}
