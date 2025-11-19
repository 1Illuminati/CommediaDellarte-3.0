package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerDeathEvent extends AreaEntityEvent<PlayerDeathEvent> {
    public AreaPlayerDeathEvent(Area area, PlayerDeathEvent event) {
        super(area, event);
    }

    @Override
    public Player getEntity() {
        return this.getEvent().getEntity();
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerDeathEvent.class, k -> new HandlerList());
    }
}
