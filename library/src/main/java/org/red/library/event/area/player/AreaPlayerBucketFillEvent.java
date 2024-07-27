
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.red.library.world.Area;

public class AreaPlayerBucketFillEvent extends AreaPlayerEvent<PlayerBucketFillEvent> {
    public AreaPlayerBucketFillEvent(Area area, PlayerBucketFillEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerBucketFillEvent.class, k -> new HandlerList());
    }
}
