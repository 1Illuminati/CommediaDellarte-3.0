
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.red.library.world.Area;

public class AreaSpongeAbsorbEvent extends AreaBlockEvent<SpongeAbsorbEvent> {
    public AreaSpongeAbsorbEvent(Area area, SpongeAbsorbEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaSpongeAbsorbEvent.class, k -> new HandlerList());
    }
}
