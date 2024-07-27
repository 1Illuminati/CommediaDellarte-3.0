
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBucketEvent;
import org.red.library.world.Area;

public class AreaPlayerBucketEvent extends AreaPlayerEvent<PlayerBucketEvent> {
    public AreaPlayerBucketEvent(Area area, PlayerBucketEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerBucketEvent.class, k -> new HandlerList());
    }
}
