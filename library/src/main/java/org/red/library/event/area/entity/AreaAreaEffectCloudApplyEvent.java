package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.red.library.world.Area;

public class AreaAreaEffectCloudApplyEvent extends AreaEntityEvent<AreaEffectCloudApplyEvent> {
    public AreaAreaEffectCloudApplyEvent(Area area, AreaEffectCloudApplyEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaAreaEffectCloudApplyEvent.class, k -> new HandlerList());
    }
}
