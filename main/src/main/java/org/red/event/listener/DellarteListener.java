package org.red.event.listener;

import org.red.CommediaDellartePlugin;
import org.red.library.event.listener.A_Listener;

public abstract class DellarteListener extends A_Listener {

    public void register() {
        this.register(CommediaDellartePlugin.instance);
    }
}
