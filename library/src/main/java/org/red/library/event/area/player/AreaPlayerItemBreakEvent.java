
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.red.library.world.Area;

public class AreaPlayerItemBreakEvent extends AreaPlayerEvent<PlayerItemBreakEvent> {
    public AreaPlayerItemBreakEvent(Area area, PlayerItemBreakEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerItemBreakEvent.class, k -> new HandlerList());
    }
}
