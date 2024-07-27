
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.MoistureChangeEvent;
import org.red.library.world.Area;

public class AreaMoistureChangeEvent extends AreaBlockEvent<MoistureChangeEvent> {
    public AreaMoistureChangeEvent(Area area, MoistureChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaMoistureChangeEvent.class, k -> new HandlerList());
    }
}
