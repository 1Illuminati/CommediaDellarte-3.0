
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.red.library.world.Area;

public class AreaEntityBlockFormEvent extends AreaBlockEvent<EntityBlockFormEvent> {
    public AreaEntityBlockFormEvent(Area area, EntityBlockFormEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityBlockFormEvent.class, k -> new HandlerList());
    }
}
