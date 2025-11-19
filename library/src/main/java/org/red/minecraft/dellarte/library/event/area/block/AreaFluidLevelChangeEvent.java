
package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaFluidLevelChangeEvent extends AreaBlockEvent<FluidLevelChangeEvent> {
    public AreaFluidLevelChangeEvent(Area area, FluidLevelChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaFluidLevelChangeEvent.class, k -> new HandlerList());
    }
}
