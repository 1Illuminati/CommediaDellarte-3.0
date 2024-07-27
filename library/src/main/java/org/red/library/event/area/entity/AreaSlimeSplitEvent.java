package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.red.library.world.Area;

public class AreaSlimeSplitEvent extends AreaEntityEvent<SlimeSplitEvent> {
    public AreaSlimeSplitEvent(Area area, SlimeSplitEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaSlimeSplitEvent.class, k -> new HandlerList());
    }
}
