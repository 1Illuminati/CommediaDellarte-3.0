package org.red.minecraft.dellarte.user;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.user.A_OfflinePlayer;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.CommediaDellartePlugin;

import java.util.UUID;

public final class A_OfflinePlayerImpl implements A_OfflinePlayer {
    private OfflinePlayer offlinePlayer;

    public A_OfflinePlayerImpl(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }

    public void updateOfflinePlayer() {
        offlinePlayer = Bukkit.getOfflinePlayer(offlinePlayer.getUniqueId());
        CommediaDellartePlugin.sendDebugLog("Updated OfflinePlayer name: " + offlinePlayer.getName() + " uuid: " + offlinePlayer.getUniqueId());
    }

    @Override
    public void setOp(boolean b) {
        offlinePlayer.setOp(b);
    }

    @Override
    public ItemStack getPlayerSkull() {
        return null;
    }

    @Override
    public boolean isOp() {
        return offlinePlayer.isOp();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
        offlinePlayer.setStatistic(statistic, entityType, i);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
        offlinePlayer.decrementStatistic(statistic, entityType, i);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) throws IllegalArgumentException {
        offlinePlayer.incrementStatistic(statistic, entityType, i);
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        return offlinePlayer.getStatistic(statistic, entityType);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        offlinePlayer.decrementStatistic(statistic, entityType);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        offlinePlayer.incrementStatistic(statistic, entityType);
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        offlinePlayer.setStatistic(statistic, material, i);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        offlinePlayer.decrementStatistic(statistic, material, i);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        offlinePlayer.incrementStatistic(statistic, material, i);
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        return offlinePlayer.getStatistic(statistic, material);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        offlinePlayer.decrementStatistic(statistic, material);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        offlinePlayer.incrementStatistic(statistic, material);
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        return offlinePlayer.getStatistic(statistic);
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        offlinePlayer.setStatistic(statistic, i);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        offlinePlayer.decrementStatistic(statistic, i);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        offlinePlayer.incrementStatistic(statistic, i);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        offlinePlayer.decrementStatistic(statistic);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        offlinePlayer.incrementStatistic(statistic);
    }

    @Override
    @Nullable
    public Location getBedSpawnLocation() {
        return offlinePlayer.getBedSpawnLocation();
    }

    @Override
    public boolean hasPlayedBefore() {
        return offlinePlayer.hasPlayedBefore();
    }

    @Override
    public long getLastPlayed() {
        return offlinePlayer.getLastPlayed();
    }

    @Override
    public long getFirstPlayed() {
        return offlinePlayer.getFirstPlayed();
    }

    @Override
    @Nullable
    public Player getPlayer() {
        return offlinePlayer.getPlayer();
    }

    @Override
    public @Nullable A_Player getAPlayer() {
        return null;
    }

    @Override
    public void setWhitelisted(boolean b) {
        offlinePlayer.setWhitelisted(b);
    }

    @Override
    public boolean isWhitelisted() {
        return offlinePlayer.isWhitelisted();
    }

    @Override
    public boolean isBanned() {
        return offlinePlayer.isBanned();
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        return offlinePlayer.getUniqueId();
    }

    @Override
    @Nullable
    public String getName() {
        return offlinePlayer.getName();
    }

    @Override
    public boolean isOnline() {
        return offlinePlayer.isOnline();
    }

    @Override
    @NotNull
    public A_DataMap getDataMap() {
        return getDataMap(CommediaDellartePlugin.instance);
    }

    @Override
    @NotNull
    public A_DataMap getDataMap(Plugin plugin) {
        return CommediaDellarte.getStorage(new NamespacedKey(plugin, "player")).getDataMap(getUniqueId());
    }

    @Override
    @NotNull
    public CoolTimeMap getCoolTime() {
        return getCoolTime(CommediaDellartePlugin.instance);
    }

    @Override
    @NotNull
    public CoolTimeMap getCoolTime(Plugin plugin) {
        return CommediaDellarte.getStorage(new NamespacedKey(plugin, "player")).getCoolTimeMap(getUniqueId());
    }
}
