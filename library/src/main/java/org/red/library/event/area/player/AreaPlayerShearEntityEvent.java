
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.red.library.world.Area;

public class AreaPlayerShearEntityEvent extends AreaPlayerEvent<PlayerShearEntityEvent> {
    public AreaPlayerShearEntityEvent(Area area, PlayerShearEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerShearEntityEvent.class, k -> new HandlerList());
    }
}
