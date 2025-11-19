
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerStatisticIncrementEvent extends AreaPlayerEvent<PlayerStatisticIncrementEvent> {
    public AreaPlayerStatisticIncrementEvent(Area area, PlayerStatisticIncrementEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerStatisticIncrementEvent.class, k -> new HandlerList());
    }
}
