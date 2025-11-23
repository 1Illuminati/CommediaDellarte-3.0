package org.red.minecraft.dellarte.library;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.library.data.serialize.RegisterSerializable;
import org.red.minecraft.dellarte.library.data.IDataStroage;
import org.red.minecraft.dellarte.library.entity.A_Entity;
import org.red.minecraft.dellarte.library.entity.A_LivingEntity;
import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.interactive.InteractiveManager;
import org.red.minecraft.dellarte.library.user.A_OfflinePlayer;
import org.red.minecraft.dellarte.library.util.BossBarTimer;
import org.red.minecraft.dellarte.library.util.Timer;
import org.red.minecraft.dellarte.library.world.A_World;

import java.util.UUID;

public interface IDellarteManager {
    
    <T> void registerSerializableClass(Class<T> clazz, RegisterSerializable<T> registerSerializable);

    IDataStroage getStorage(NamespacedKey key);

    boolean containStorage(NamespacedKey key);
    
    @Nullable
    A_Player getAPlayer(String name);

    @Nullable
    A_Player getAPlayer(UUID uuid);

    A_Player getAPlayer(@NotNull Player player);

    @Nullable
    A_Player getAPlayer(@NotNull OfflinePlayer player);

    A_OfflinePlayer getAOfflinePlayer(@NotNull OfflinePlayer player);

    A_Entity getAEntity(@NotNull Entity entity);

    A_LivingEntity getALivingEntity(@NotNull LivingEntity entity);

    A_World getAWorld(@NotNull World world);

    @Nullable
    A_World getAWorld(String worldName);

    @Nullable
    A_World getAWorld(UUID worldUUID);

    <T> InteractiveManager<T> getInteractiveManager(@NotNull Class<T> clazz);

    <T> boolean setInteractiveManager(@NotNull Class<T> clazz, @NotNull InteractiveManager<T> manager);
    
    Timer createTimer(@NotNull NamespacedKey key, int maxTime, @Nullable Runnable runnable);

    BossBarTimer createBossBarTimer(NamespacedKey key, int maxTime, @Nullable Runnable runnable, BossBar... bossBars);
}
