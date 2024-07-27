
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.red.library.world.Area;

public class AreaPlayerGameModeChangeEvent extends AreaPlayerEvent<PlayerGameModeChangeEvent> {
    public AreaPlayerGameModeChangeEvent(Area area, PlayerGameModeChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerGameModeChangeEvent.class, k -> new HandlerList());
    }
}
