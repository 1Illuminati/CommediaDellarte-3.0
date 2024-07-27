
package org.red.library.event.area.player;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.red.library.world.Area;

public class AreaPlayerUnleashEntityEvent extends AreaPlayerEvent<PlayerAnimationEvent> {
    public AreaPlayerUnleashEntityEvent(Area area, PlayerUnleashEntityEvent event) {
        super(area, null);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerUnleashEntityEvent.class, k -> new HandlerList());
    }
}
