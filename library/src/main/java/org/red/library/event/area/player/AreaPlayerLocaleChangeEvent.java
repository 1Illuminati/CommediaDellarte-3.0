
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.red.library.world.Area;

public class AreaPlayerLocaleChangeEvent extends AreaPlayerEvent<PlayerLocaleChangeEvent> {
    public AreaPlayerLocaleChangeEvent(Area area, PlayerLocaleChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerLocaleChangeEvent.class, k -> new HandlerList());
    }
}
