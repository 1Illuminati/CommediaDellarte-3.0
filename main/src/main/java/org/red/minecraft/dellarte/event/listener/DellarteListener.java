package org.red.minecraft.dellarte.event.listener;

import org.red.minecraft.dellarte.library.event.listener.A_Listener;
import org.red.minecraft.dellarte.CommediaDellartePlugin;

public abstract class DellarteListener extends A_Listener {

    public void register() {
        this.register(CommediaDellartePlugin.instance);
    }
}
