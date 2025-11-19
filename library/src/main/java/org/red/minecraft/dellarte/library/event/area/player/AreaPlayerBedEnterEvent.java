
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerBedEnterEvent extends AreaPlayerEvent<PlayerBedEnterEvent> {
    public AreaPlayerBedEnterEvent(Area area, PlayerBedEnterEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerBedEnterEvent.class, k -> new HandlerList());
    }
}
