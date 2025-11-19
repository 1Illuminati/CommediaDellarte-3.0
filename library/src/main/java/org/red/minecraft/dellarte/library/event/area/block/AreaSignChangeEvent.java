
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SignChangeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaSignChangeEvent extends AreaBlockEvent<SignChangeEvent> {
    public AreaSignChangeEvent(Area area, SignChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaSignChangeEvent.class, k -> new HandlerList());
    }
}
