
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerCommandSendEvent extends AreaPlayerEvent<PlayerCommandSendEvent> {
    public AreaPlayerCommandSendEvent(Area area, PlayerCommandSendEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerCommandSendEvent.class, k -> new HandlerList());
    }
}
