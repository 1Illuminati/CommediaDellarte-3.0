
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerCommandPreprocessEvent extends AreaPlayerEvent<PlayerCommandPreprocessEvent> {
    public AreaPlayerCommandPreprocessEvent(Area area, PlayerCommandPreprocessEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerCommandPreprocessEvent.class, k -> new HandlerList());
    }
}
