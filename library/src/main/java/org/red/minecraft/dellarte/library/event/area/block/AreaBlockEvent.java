package org.red.minecraft.dellarte.library.event.area.block;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockEvent;
import org.red.minecraft.dellarte.library.event.area.AreaEvent;
import org.red.minecraft.dellarte.library.world.Area;

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
