
package org.red.minecraft.dellarte.library.event.area.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaPlayerMoveEvent extends AreaPlayerEvent<PlayerMoveEvent> {
    private final boolean isIn;
    private final boolean isOut;

    public AreaPlayerMoveEvent(Area area, PlayerMoveEvent event) {
        super(area, event);
        isIn = getArea().contain(getEvent().getTo()) && !getArea().contain(getEvent().getFrom());
        isOut = !getArea().contain(getEvent().getTo()) && getArea().contain(getEvent().getFrom());
    }

    public boolean isIn() {
        return isIn;
    }

    public boolean isOut() {
        return isOut;
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaPlayerMoveEvent.class, k -> new HandlerList());
    }
}
