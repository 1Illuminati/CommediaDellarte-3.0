
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerEditBookEvent extends AreaPlayerEvent<PlayerEditBookEvent> {
    public AreaPlayerEditBookEvent(Area area, PlayerEditBookEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerEditBookEvent.class, k -> new HandlerList());
    }
}
