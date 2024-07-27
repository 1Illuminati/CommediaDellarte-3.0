package org.red.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.red.library.event.area.player.AreaPlayerMoveEvent;

public class AreaPlayerMoveEventListener extends AreaPlayerEventListener<PlayerMoveEvent> {
    public AreaPlayerMoveEventListener() {
        super(AreaPlayerMoveEvent.class);
    }

    @Override
    @EventHandler
    public void onEvent(PlayerMoveEvent event) {
        runAreaEvent(event, event.getFrom(), event.getTo());
    }
}
