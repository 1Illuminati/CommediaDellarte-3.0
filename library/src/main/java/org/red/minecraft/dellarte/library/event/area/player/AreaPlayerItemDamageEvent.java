
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerItemDamageEvent extends AreaPlayerEvent<PlayerItemDamageEvent> {
    public AreaPlayerItemDamageEvent(Area area, PlayerItemDamageEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerItemDamageEvent.class, k -> new HandlerList());
    }
}
