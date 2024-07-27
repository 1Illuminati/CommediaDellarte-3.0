
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.red.library.world.Area;

public class AreaPlayerExpChangeEvent extends AreaPlayerEvent<PlayerExpChangeEvent> {
    public AreaPlayerExpChangeEvent(Area area, PlayerExpChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerExpChangeEvent.class, k -> new HandlerList());
    }
}
