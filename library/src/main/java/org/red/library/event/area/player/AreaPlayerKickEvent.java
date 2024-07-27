
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerKickEvent;
import org.red.library.world.Area;

public class AreaPlayerKickEvent extends AreaPlayerEvent<PlayerKickEvent> {
    public AreaPlayerKickEvent(Area area, PlayerKickEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerKickEvent.class, k -> new HandlerList());
    }
}
