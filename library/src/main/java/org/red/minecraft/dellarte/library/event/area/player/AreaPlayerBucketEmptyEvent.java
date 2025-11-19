
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerBucketEmptyEvent extends AreaPlayerEvent<PlayerBucketEmptyEvent> {
    public AreaPlayerBucketEmptyEvent(Area area, PlayerBucketEmptyEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerBucketEmptyEvent.class, k -> new HandlerList());
    }
}
