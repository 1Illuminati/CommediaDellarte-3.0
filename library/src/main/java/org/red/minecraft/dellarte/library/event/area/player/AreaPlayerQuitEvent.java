
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerQuitEvent extends AreaPlayerEvent<PlayerQuitEvent> {
    public AreaPlayerQuitEvent(Area area, PlayerQuitEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerQuitEvent.class, k -> new HandlerList());
    }
}
