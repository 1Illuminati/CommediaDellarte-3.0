
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerBucketEntityEvent extends AreaPlayerEvent<PlayerBucketEntityEvent> {
    public AreaPlayerBucketEntityEvent(Area area, PlayerBucketEntityEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerBucketEntityEvent.class, k -> new HandlerList());
    }
}
