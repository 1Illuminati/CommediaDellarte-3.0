package org.red.minecraft.dellarte.event.listener.area;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.red.minecraft.dellarte.library.event.area.player.AreaPlayerMoveEvent;

public class PlayerMoveEventListener extends AllPlayerEventListener<PlayerMoveEvent> {
    public PlayerMoveEventListener() {
        super(AreaPlayerMoveEvent.class, PlayerMoveEvent.class);
        register();
    }

    @Override
    @EventHandler
    public void onEvent(PlayerMoveEvent event) {
        runAreaEvent(event, event.getFrom(), event.getTo());
    }
}
