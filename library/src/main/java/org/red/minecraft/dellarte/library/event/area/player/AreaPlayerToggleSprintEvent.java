
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerToggleSprintEvent extends AreaPlayerEvent<PlayerToggleSprintEvent> {
    public AreaPlayerToggleSprintEvent(Area area, PlayerToggleSprintEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerToggleSprintEvent.class, k -> new HandlerList());
    }
}
