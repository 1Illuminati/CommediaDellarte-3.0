package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaEnderDragonChangePhaseEvent extends AreaEntityEvent<EnderDragonChangePhaseEvent> {
    public AreaEnderDragonChangePhaseEvent(Area area, EnderDragonChangePhaseEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEnderDragonChangePhaseEvent.class, k -> new HandlerList());
    }
}
