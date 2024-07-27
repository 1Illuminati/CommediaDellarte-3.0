
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.red.library.world.Area;

public class AreaPlayerPickupItemEvent extends AreaPlayerEvent<PlayerPickupItemEvent> {
    public AreaPlayerPickupItemEvent(Area area, PlayerPickupItemEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerPickupItemEvent.class, k -> new HandlerList());
    }
}
