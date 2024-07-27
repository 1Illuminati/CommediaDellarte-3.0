
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.red.library.world.Area;

public class AreaPlayerRegisterChannelEvent extends AreaPlayerEvent<PlayerRegisterChannelEvent> {
    public AreaPlayerRegisterChannelEvent(Area area, PlayerRegisterChannelEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerRegisterChannelEvent.class, k -> new HandlerList());
    }
}
