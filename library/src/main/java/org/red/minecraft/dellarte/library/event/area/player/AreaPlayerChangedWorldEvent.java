
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerChangedWorldEvent extends AreaPlayerEvent<PlayerChangedWorldEvent> {
    public AreaPlayerChangedWorldEvent(Area area, PlayerChangedWorldEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerChangedWorldEvent.class, k -> new HandlerList());
    }
}
