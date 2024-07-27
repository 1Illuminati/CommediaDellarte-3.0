package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.HorseJumpEvent;
import org.red.library.world.Area;

public class AreaHorseJumpEvent extends AreaEntityEvent<HorseJumpEvent> {
    public AreaHorseJumpEvent(Area area, HorseJumpEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaHorseJumpEvent.class, k -> new HandlerList());
    }
}
