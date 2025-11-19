
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerInteractAtEntityEvent extends AreaPlayerEvent<PlayerInteractAtEntityEvent> {
    public AreaPlayerInteractAtEntityEvent(Area area, PlayerInteractAtEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerInteractAtEntityEvent.class, k -> new HandlerList());
    }
}
