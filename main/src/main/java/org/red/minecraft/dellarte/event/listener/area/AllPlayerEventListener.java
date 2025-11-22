package org.red.minecraft.dellarte.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEvent;
import org.red.minecraft.dellarte.library.event.area.player.AreaPlayerEvent;

public class AllPlayerEventListener<T extends PlayerEvent> extends AllEventListener<T> {
    public AllPlayerEventListener(Class<? extends AreaPlayerEvent<T>> clazz, Class<T> eventClass) {
        super(clazz, eventClass);
        this.register();
    }

    @Override
    @EventHandler
    public void onEvent(T event) {
        runAreaEvent(event, event.getPlayer().getLocation());
    }
}
