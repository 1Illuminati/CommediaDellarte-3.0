
package org.red.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBucketFishEvent;
import org.red.library.world.Area;

public class AreaPlayerBucketFishEvent extends AreaPlayerEvent<PlayerBucketFishEvent> {
    public AreaPlayerBucketFishEvent(Area area, PlayerBucketFishEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerBucketFishEvent.class, k -> new HandlerList());
    }
}
