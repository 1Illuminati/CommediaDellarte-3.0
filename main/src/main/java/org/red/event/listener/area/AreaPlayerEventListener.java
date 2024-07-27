package org.red.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEvent;
import org.red.library.event.area.player.AreaPlayerEvent;

public class AreaPlayerEventListener<T extends PlayerEvent> extends AreaEventListener<T> {
    public AreaPlayerEventListener(Class<? extends AreaPlayerEvent<T>> clazz) {
        super(clazz);
        this.register();
    }

    @Override
    @EventHandler
    public void onEvent(T event) {
        runAreaEvent(event, event.getPlayer().getLocation());
    }
}
