
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.red.library.world.Area;

public class AreaPlayerRecipeDiscoverEvent extends AreaPlayerEvent<PlayerRecipeDiscoverEvent> {
    public AreaPlayerRecipeDiscoverEvent(Area area, PlayerRecipeDiscoverEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerRecipeDiscoverEvent.class, k -> new HandlerList());
    }
}
