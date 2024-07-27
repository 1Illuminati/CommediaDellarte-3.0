
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.red.library.world.Area;

public class AreaPlayerBedLeaveEvent extends AreaPlayerEvent<PlayerBedLeaveEvent> {
    public AreaPlayerBedLeaveEvent(Area area, PlayerBedLeaveEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerBedLeaveEvent.class, k -> new HandlerList());
    }
}
