
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerTakeLecternBookEvent extends AreaPlayerEvent<PlayerTakeLecternBookEvent> {
    public AreaPlayerTakeLecternBookEvent(Area area, PlayerTakeLecternBookEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerTakeLecternBookEvent.class, k -> new HandlerList());
    }
}
