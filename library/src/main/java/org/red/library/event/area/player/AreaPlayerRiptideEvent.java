
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.red.library.world.Area;

public class AreaPlayerRiptideEvent extends AreaPlayerEvent<PlayerRiptideEvent> {
    public AreaPlayerRiptideEvent(Area area, PlayerRiptideEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerRiptideEvent.class, k -> new HandlerList());
    }
}
