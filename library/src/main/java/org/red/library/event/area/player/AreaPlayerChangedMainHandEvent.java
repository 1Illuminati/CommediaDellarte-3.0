
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.red.library.world.Area;

public class AreaPlayerChangedMainHandEvent extends AreaPlayerEvent<PlayerChangedMainHandEvent> {
    public AreaPlayerChangedMainHandEvent(Area area, PlayerChangedMainHandEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerChangedMainHandEvent.class, k -> new HandlerList());
    }
}
