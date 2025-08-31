package org.red.library;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.library.entity.A_Entity;
import org.red.library.entity.A_LivingEntity;
import org.red.library.entity.A_Player;
import org.red.library.interactive.InteractiveManager;
import org.red.library.user.A_OfflinePlayer;
import org.red.library.util.BossBarTimer;
import org.red.library.util.Timer;
import org.red.library.world.A_World;
import org.red.library.util.PluginData;

import java.util.UUID;

public final class CommediaDellarte {
    private static IDellarteManager manager;
    public static void setDellarteManager(IDellarteManager manager) {
        CommediaDellarte.manager = manager;
    }

    public static @Nullable A_Player getAPlayer(String name) {
        return manager.getAPlayer(name);
    }

    public static A_OfflinePlayer getAOfflinePlayer(@NotNull OfflinePlayer player) {
        return manager.getAOfflinePlayer(player);
    }

    public static @Nullable A_Player getAPlayer(UUID uuid) {
        return manager.getAPlayer(uuid);
    }

    public static A_Player getAPlayer(@NotNull Player player) {
        return manager.getAPlayer(player);
    }

    public static @Nullable A_Player getAPlayer(@NotNull OfflinePlayer player) {
        return manager.getAPlayer(player);
    }

    public static A_Entity getAEntity(@NotNull Entity entity) {
        return manager.getAEntity(entity);
    }

    public static A_LivingEntity getALivingEntity(@NotNull LivingEntity entity) {
        return manager.getALivingEntity(entity);
    }

    public static A_World getAWorld(@NotNull World world) {
        return manager.getAWorld(world);
    }

    public static @Nullable A_World getAWorld(String worldName) {
        return manager.getAWorld(worldName);
    }

    public static @Nullable A_World getAWorld(UUID worldUUID) {
        return manager.getAWorld(worldUUID);
    }

    public static <T> InteractiveManager<T> getInteractiveManager(@NotNull Class<T> managerType) {
        return manager.getInteractiveManager(managerType);
    }

    public static <T> boolean setInteractiveManager(@NotNull Class<T> clazz, @NotNull InteractiveManager<T> manager) {
        return CommediaDellarte.manager.setInteractiveManager(clazz, manager);
    }

    public static Timer createTimer(@NotNull NamespacedKey key, int maxTime, @Nullable Runnable runnable) {
        return manager.createTimer(key, maxTime, runnable);
    }

    public static BossBarTimer createBossBarTimer(NamespacedKey key, int maxTime, @Nullable Runnable runnable, BossBar... bossBars) {
        return manager.createBossBarTimer(key, maxTime, runnable, bossBars);
    }

    public static PluginData getPluginData(Plugin plugin) {
        return manager.getPluginData(plugin);
    }
}
