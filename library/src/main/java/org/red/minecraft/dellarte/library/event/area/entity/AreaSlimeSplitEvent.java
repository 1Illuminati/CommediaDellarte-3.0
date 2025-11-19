package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaSlimeSplitEvent extends AreaEntityEvent<SlimeSplitEvent> {
    public AreaSlimeSplitEvent(Area area, SlimeSplitEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaSlimeSplitEvent.class, k -> new HandlerList());
    }
}
