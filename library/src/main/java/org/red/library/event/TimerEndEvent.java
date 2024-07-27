package org.red.library.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.red.library.util.Timer;

public class TimerEndEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Timer timer;

    public TimerEndEvent(Timer timer) {
        this.timer = timer;
    }

    public Timer getTimer() {
        return this.timer;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
