package org.red.util;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.CommediaDellartePlugin;
import org.red.library.event.TimerEndEvent;
import org.red.library.util.BossBarTimer;

import java.util.ArrayList;
import java.util.List;

public class BossBarTimerImpl extends TimerImpl implements BossBarTimer {
    private final List<BossBar> bossBars = new ArrayList<>();

    public BossBarTimerImpl(@NotNull NamespacedKey key, int maxTime, @Nullable Runnable runnable, BossBar... bossBars) {
        super(key, maxTime, runnable);
        this.bossBars.addAll(List.of(bossBars));
    }

    @Override
    public void addBossBar(BossBar bossBar) {
        this.bossBars.add(bossBar);
    }

    @Override
    public List<BossBar> getBossBars() {
        return bossBars;
    }

    @Override
    public void removeBossBar(BossBar bossBar) {
        this.bossBars.remove(bossBar);
    }

    @Override
    public boolean containsBossBar(BossBar bossBar) {
        return this.bossBars.contains(bossBar);
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
                    Bukkit.getScheduler().runTask(CommediaDellartePlugin.instance, () -> Bukkit.getPluginManager().callEvent(new TimerEndEvent(BossBarTimerImpl.this)));
                    this.cancel();
                    return;
                }
                Bukkit.getScheduler().runTask(CommediaDellartePlugin.instance, () -> bossBars.forEach(bossBar -> bossBar.setProgress((double) getTime() / (double) getMaxTime())));
                if (!nullCheck) getRunnable().run();
                time += 1;
            }
        }.runTaskTimerAsynchronously(CommediaDellartePlugin.instance, 1, 0);
    }
}
