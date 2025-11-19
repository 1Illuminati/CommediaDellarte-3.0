
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerPickupArrowEvent extends AreaPlayerEvent<PlayerPickupArrowEvent> {
    public AreaPlayerPickupArrowEvent(Area area, PlayerPickupArrowEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerPickupArrowEvent.class, k -> new HandlerList());
    }
}
