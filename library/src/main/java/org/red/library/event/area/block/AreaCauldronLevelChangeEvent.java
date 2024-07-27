
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.red.library.world.Area;

public class AreaCauldronLevelChangeEvent extends AreaBlockEvent<CauldronLevelChangeEvent> {
    public AreaCauldronLevelChangeEvent(Area area, CauldronLevelChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaCauldronLevelChangeEvent.class, k -> new HandlerList());
    }
}
