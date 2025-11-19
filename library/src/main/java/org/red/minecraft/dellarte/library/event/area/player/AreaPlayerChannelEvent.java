
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChannelEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerChannelEvent extends AreaPlayerEvent<PlayerChannelEvent> {
    public AreaPlayerChannelEvent(Area area, PlayerChannelEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerChannelEvent.class, k -> new HandlerList());
    }
}
