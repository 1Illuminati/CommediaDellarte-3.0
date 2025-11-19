
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerItemHeldEvent extends AreaPlayerEvent<PlayerItemHeldEvent> {
    public AreaPlayerItemHeldEvent(Area area, PlayerItemHeldEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerItemHeldEvent.class, k -> new HandlerList());
    }
}
