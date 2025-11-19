package org.red.minecraft.dellarte.library.util;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Timer {
    void start();
    void stop();

    boolean isRunning();

    void addTime(int time);
    void addMaxTime(int maxTime);
    void setTime(int time);
    void setMaxTime(int maxTime);
    int getTime();
    int getMaxTime();
    @NotNull NamespacedKey getKey();
    @Nullable  Runnable getRunnable();
}
