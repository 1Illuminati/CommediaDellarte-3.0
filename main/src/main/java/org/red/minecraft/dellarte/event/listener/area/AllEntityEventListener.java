package org.red.minecraft.dellarte.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityEvent;
import org.red.minecraft.dellarte.library.event.area.entity.AreaEntityEvent;

public class AllEntityEventListener<T extends EntityEvent> extends AllEventListener<T> {

    public AllEntityEventListener(Class<? extends AreaEntityEvent<T>> clazz, Class<T> eventClass) {
        super(clazz, eventClass);
        this.register();
    }

    @Override
    @EventHandler
    public void onEvent(T event) {
        runAreaEvent(event, event.getEntity().getLocation());
    }
}
