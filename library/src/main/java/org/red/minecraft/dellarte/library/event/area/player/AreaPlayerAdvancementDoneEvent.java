package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerAdvancementDoneEvent extends AreaPlayerEvent<PlayerAdvancementDoneEvent> {
    public AreaPlayerAdvancementDoneEvent(Area area, PlayerAdvancementDoneEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerAdvancementDoneEvent.class, k -> new HandlerList());
    }
}
