package org.red.library.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import java.util.Arrays;

public class FirstLoadEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public FirstLoadEvent() {
        Arrays.stream(HANDLERS_LIST.getRegisteredListeners()).forEach(registeredListener -> {
            Bukkit.getConsoleSender().sendMessage(registeredListener.getPlugin().getName());
        });
        Bukkit.getConsoleSender().sendMessage("TESTTESTTESTTEST222");
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
