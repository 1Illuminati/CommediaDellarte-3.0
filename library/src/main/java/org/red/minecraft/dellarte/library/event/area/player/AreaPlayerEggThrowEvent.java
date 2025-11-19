
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerEggThrowEvent extends AreaPlayerEvent<PlayerEggThrowEvent> {
    public AreaPlayerEggThrowEvent(Area area, PlayerEggThrowEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerEggThrowEvent.class, k -> new HandlerList());
    }
}
