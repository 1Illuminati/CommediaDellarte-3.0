
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.red.library.world.Area;

public class AreaBlockReceiveGameEvent extends AreaBlockEvent<BlockReceiveGameEvent> {
    public AreaBlockReceiveGameEvent(Area area, BlockReceiveGameEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaBlockReceiveGameEvent.class, k -> new HandlerList());
    }
}
