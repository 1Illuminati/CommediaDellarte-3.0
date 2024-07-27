
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.red.library.world.Area;

public class AreaPlayerHarvestBlockEvent extends AreaPlayerEvent<PlayerHarvestBlockEvent> {
    public AreaPlayerHarvestBlockEvent(Area area, PlayerHarvestBlockEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerHarvestBlockEvent.class, k -> new HandlerList());
    }
}
