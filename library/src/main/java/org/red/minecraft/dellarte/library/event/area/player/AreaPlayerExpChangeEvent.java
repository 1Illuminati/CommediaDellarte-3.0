
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerExpChangeEvent extends AreaPlayerEvent<PlayerExpChangeEvent> {
    public AreaPlayerExpChangeEvent(Area area, PlayerExpChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerExpChangeEvent.class, k -> new HandlerList());
    }
}
