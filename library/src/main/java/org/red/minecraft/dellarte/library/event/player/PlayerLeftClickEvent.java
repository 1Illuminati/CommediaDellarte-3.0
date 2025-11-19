package org.red.minecraft.dellarte.library.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.event.A_PlayerEvent;

public class PlayerLeftClickEvent extends A_PlayerEvent {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final PlayerInteractEvent event;

    public PlayerLeftClickEvent(@NotNull A_Player who, PlayerInteractEvent event) {
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
