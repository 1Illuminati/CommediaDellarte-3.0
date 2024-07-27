
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.red.library.world.Area;

public class AreaPlayerTeleportEvent extends AreaPlayerEvent<PlayerTeleportEvent> {
    public AreaPlayerTeleportEvent(Area area, PlayerTeleportEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerTeleportEvent.class, k -> new HandlerList());
    }
}
