
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.red.library.world.Area;

public class AreaPlayerRespawnEvent extends AreaPlayerEvent<PlayerRespawnEvent> {
    public AreaPlayerRespawnEvent(Area area, PlayerRespawnEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerRespawnEvent.class, k -> new HandlerList());
    }
}
