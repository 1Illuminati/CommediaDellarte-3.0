
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerArmorStandManipulateEvent extends AreaPlayerEvent<PlayerArmorStandManipulateEvent> {
    public AreaPlayerArmorStandManipulateEvent(Area area, PlayerArmorStandManipulateEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerArmorStandManipulateEvent.class, k -> new HandlerList());
    }
}
