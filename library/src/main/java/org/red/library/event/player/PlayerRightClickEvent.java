package org.red.library.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.red.library.entity.A_Player;
import org.red.library.event.A_PlayerEvent;

public class PlayerRightClickEvent extends A_PlayerEvent {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final PlayerInteractEvent event;

    public PlayerRightClickEvent(@NotNull A_Player who, PlayerInteractEvent event) {
        super(who);
        this.event = event;
    }

    public PlayerInteractEvent getEvent() {
        return this.event;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
