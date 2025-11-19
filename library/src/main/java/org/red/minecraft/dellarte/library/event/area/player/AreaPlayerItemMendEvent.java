
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerItemMendEvent extends AreaPlayerEvent<PlayerItemMendEvent> {
    public AreaPlayerItemMendEvent(Area area, PlayerItemMendEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerItemMendEvent.class, k -> new HandlerList());
    }
}
