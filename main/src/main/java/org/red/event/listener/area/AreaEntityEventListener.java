package org.red.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityEvent;
import org.red.library.event.area.entity.AreaEntityEvent;

public class AreaEntityEventListener<T extends EntityEvent> extends AreaEventListener<T> {

    public AreaEntityEventListener(Class<? extends AreaEntityEvent<T>> clazz) {
        super(clazz);
        this.register();
    }

    @Override
    @EventHandler
    public void onEvent(T event) {
        runAreaEvent(event, event.getEntity().getLocation());
    }
}
