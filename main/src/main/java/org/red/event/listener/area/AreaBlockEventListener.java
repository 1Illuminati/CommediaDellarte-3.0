package org.red.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockEvent;
import org.red.library.event.area.block.AreaBlockEvent;

public class AreaBlockEventListener<T extends BlockEvent> extends AreaEventListener<T> {

    public AreaBlockEventListener(Class<? extends AreaBlockEvent<T>> clazz) {
        super(clazz);
        this.register();
    }

    @Override
    @EventHandler
    public void onEvent(T event) {
        runAreaEvent(event, event.getBlock().getLocation());
    }
}
