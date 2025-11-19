
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerChangedMainHandEvent extends AreaPlayerEvent<PlayerChangedMainHandEvent> {
    public AreaPlayerChangedMainHandEvent(Area area, PlayerChangedMainHandEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerChangedMainHandEvent.class, k -> new HandlerList());
    }
}
