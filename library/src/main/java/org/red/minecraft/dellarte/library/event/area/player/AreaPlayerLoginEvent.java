
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLoginEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerLoginEvent extends AreaPlayerEvent<PlayerLoginEvent> {
    public AreaPlayerLoginEvent(Area area, PlayerLoginEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerLoginEvent.class, k -> new HandlerList());
    }
}
