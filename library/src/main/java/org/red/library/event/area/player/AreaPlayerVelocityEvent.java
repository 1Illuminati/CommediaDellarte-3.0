
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.red.library.world.Area;

public class AreaPlayerVelocityEvent extends AreaPlayerEvent<PlayerVelocityEvent> {
    public AreaPlayerVelocityEvent(Area area, PlayerVelocityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerVelocityEvent.class, k -> new HandlerList());
    }
}
