
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.red.library.world.Area;

public class AreaPlayerItemConsumeEvent extends AreaPlayerEvent<PlayerItemConsumeEvent> {
    public AreaPlayerItemConsumeEvent(Area area, PlayerItemConsumeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerItemConsumeEvent.class, k -> new HandlerList());
    }
}
