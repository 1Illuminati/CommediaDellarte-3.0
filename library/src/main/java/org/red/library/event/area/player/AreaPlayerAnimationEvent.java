
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.red.library.world.Area;

public class AreaPlayerAnimationEvent extends AreaPlayerEvent<PlayerAnimationEvent> {
    public AreaPlayerAnimationEvent(Area area, PlayerAnimationEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerAnimationEvent.class, k -> new HandlerList());
    }
}
