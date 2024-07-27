package org.red.util;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.CommediaDellartePlugin;
import org.red.library.event.TimerEndEvent;
import org.red.library.util.Timer;

public class TimerImpl implements Timer {
    protected final NamespacedKey key;
    protected int maxTime;
    protected int time = 0;
    protected boolean isRunning = false;
    protected final Runnable runnable;

    public TimerImpl(@NotNull NamespacedKey key, int maxTime, @Nullable Runnable runnable) {
        this.key = key;
        this.maxTime = maxTime;
        this.runnable = runnable;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public void start() {
        if (isRunning()) {
            throw new IllegalStateException("Timer is already running");
        }

        this.setRunning(true);

        boolean nullCheck = getRunnable() == null;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getTime() >= getMaxTime() || !isRunning()) {
                    Bukkit.getScheduler().runTask(CommediaDellartePlugin.instance, () -> Bukkit.getPluginManager().callEvent(new TimerEndEvent(TimerImpl.this)));
                    this.cancel();
                    return;
                }

                if (!nullCheck) getRunnable().run();

                time += 1;
            }
        }.runTaskTimerAsynchronously(CommediaDellartePlugin.instance, 1, 0);
    }

    @Override
    public void stop() {
        this.setRunning(false);
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void addTime(int time) {
        this.setTime(this.getTime() + time);
    }

    @Override
    public void addMaxTime(int maxTime) {
        this.setMaxTime(this.getMaxTime() + maxTime);
    }

    @Override
    public void setTime(int time) {
        if (time > maxTime) throw new IllegalArgumentException("time cannot be greater than maxTime");
        this.time = time;
    }

    @Override
    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public int getMaxTime() {
        return maxTime;
    }

    @Override
    public @Nullable Runnable getRunnable() {
        return runnable;
    }
}
