package org.red.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntitySpellCastEvent;
import org.red.library.world.Area;

public class AreaEntitySpellCastEvent extends AreaEntityEvent<EntitySpellCastEvent> {
    public AreaEntitySpellCastEvent(Area area, EntitySpellCastEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaEntitySpellCastEvent.class, k -> new HandlerList());
    }
}
