
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.red.library.world.Area;

public class AreaPlayerEditBookEvent extends AreaPlayerEvent<PlayerEditBookEvent> {
    public AreaPlayerEditBookEvent(Area area, PlayerEditBookEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerEditBookEvent.class, k -> new HandlerList());
    }
}
