
package org.red.library.event.area.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.red.library.event.area.AreaEvent;
import org.red.library.world.Area;

public abstract class AreaPlayerEvent<T extends PlayerEvent> extends AreaEvent<T> {
    private final Player player;
    protected AreaPlayerEvent(Area area, T event) {
        super(area, event);

        this.player = event.getPlayer();
    }

    protected AreaPlayerEvent(Area area, T event, Player player) {
        super(area, event);

        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
