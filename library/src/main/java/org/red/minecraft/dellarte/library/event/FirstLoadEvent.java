package org.red.minecraft.dellarte.library.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FirstLoadEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public FirstLoadEvent() {
        
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
