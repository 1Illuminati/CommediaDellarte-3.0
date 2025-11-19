
package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerUnleashEntityEvent extends AreaEntityEvent<PlayerUnleashEntityEvent> {
    public AreaPlayerUnleashEntityEvent(Area area, PlayerUnleashEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerUnleashEntityEvent.class, k -> new HandlerList());
    }
}
