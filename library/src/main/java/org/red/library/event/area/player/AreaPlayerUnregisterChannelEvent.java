
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.red.library.world.Area;

public class AreaPlayerUnregisterChannelEvent extends AreaPlayerEvent<PlayerUnregisterChannelEvent> {
    public AreaPlayerUnregisterChannelEvent(Area area, PlayerUnregisterChannelEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerUnregisterChannelEvent.class, k -> new HandlerList());
    }
}
