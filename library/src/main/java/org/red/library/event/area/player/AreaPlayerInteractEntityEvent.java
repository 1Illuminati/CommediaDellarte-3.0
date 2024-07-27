
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.red.library.world.Area;

public class AreaPlayerInteractEntityEvent extends AreaPlayerEvent<PlayerInteractEntityEvent> {
    public AreaPlayerInteractEntityEvent(Area area, PlayerInteractEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerInteractEntityEvent.class, k -> new HandlerList());
    }
}
