
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.red.library.world.Area;

public class AreaPlayerLevelChangeEvent extends AreaPlayerEvent<PlayerLevelChangeEvent> {
    public AreaPlayerLevelChangeEvent(Area area, PlayerLevelChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerLevelChangeEvent.class, k -> new HandlerList());
    }
}
