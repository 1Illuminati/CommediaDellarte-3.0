
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerResourcePackStatusEvent extends AreaPlayerEvent<PlayerResourcePackStatusEvent> {
    public AreaPlayerResourcePackStatusEvent(Area area, PlayerResourcePackStatusEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerResourcePackStatusEvent.class, k -> new HandlerList());
    }
}
