
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.red.library.world.Area;

public class AreaPlayerToggleFlightEvent extends AreaPlayerEvent<PlayerToggleFlightEvent> {
    public AreaPlayerToggleFlightEvent(Area area, PlayerToggleFlightEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerToggleFlightEvent.class, k -> new HandlerList());
    }
}
