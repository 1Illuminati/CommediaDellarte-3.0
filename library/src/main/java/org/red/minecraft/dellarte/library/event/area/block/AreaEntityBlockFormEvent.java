
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEntityBlockFormEvent extends AreaBlockEvent<EntityBlockFormEvent> {
    public AreaEntityBlockFormEvent(Area area, EntityBlockFormEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntityBlockFormEvent.class, k -> new HandlerList());
    }
}
