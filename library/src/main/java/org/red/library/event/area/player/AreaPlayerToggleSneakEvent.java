
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.red.library.world.Area;

public class AreaPlayerToggleSneakEvent extends AreaPlayerEvent<PlayerToggleSneakEvent> {
    public AreaPlayerToggleSneakEvent(Area area, PlayerToggleSneakEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerToggleSneakEvent.class, k -> new HandlerList());
    }
}
