package org.red.library.event.area.block;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockEvent;
import org.red.library.event.area.AreaEvent;
import org.red.library.world.Area;

public abstract class AreaBlockEvent<T extends BlockEvent> extends AreaEvent<T> {
    private final Block block;

    protected AreaBlockEvent(Area area, T event) {
        super(area, event);
        this.block = event.getBlock();
    }

    protected AreaBlockEvent(Area area, T event, Block block) {
        super(area, event);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
