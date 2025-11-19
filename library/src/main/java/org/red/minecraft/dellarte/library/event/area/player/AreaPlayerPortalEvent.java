
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerPortalEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerPortalEvent extends AreaPlayerEvent<PlayerPortalEvent> {
    public AreaPlayerPortalEvent(Area area, PlayerPortalEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerPortalEvent.class, k -> new HandlerList());
    }
}
