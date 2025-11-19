package org.red.minecraft.dellarte.library.interactive;

import org.bukkit.Keyed;

public interface InteractiveObj<T> extends Keyed {
    void setInteractiveInObj(T obj);
    boolean isHasInteractive(T obj);
    void removeInteractive(T obj);
}



























