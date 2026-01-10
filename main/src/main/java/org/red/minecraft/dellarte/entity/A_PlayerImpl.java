package org.red.minecraft.dellarte.entity;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.inventory.CustomGui;
import org.red.minecraft.dellarte.library.user.A_OfflinePlayer;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.library.util.NamespaceMap;
import org.red.minecraft.dellarte.library.util.PairData;
import org.red.minecraft.dellarte.user.A_OfflinePlayerImpl;

import java.net.InetSocketAddress;
import java.util.*;

public class A_PlayerImpl extends A_LivingEntityImpl implements A_Player {
    private final A_OfflinePlayerImpl offlinePlayer;
    private final Player player;
    private final Map<PlayerStatus, Boolean> statusMap = new HashMap<>();
    private final NamespaceMap<PairData<PlayerChatRunnable, Boolean>> chatRunMap = new NamespaceMap<>();

    public A_PlayerImpl(A_OfflinePlayerImpl offlinePlayer, Player player) {
        super(player);
        offlinePlayer.updateOfflinePlayer();
        this.offlinePlayer = offlinePlayer;
        this.player = player;
    }

    protected A_PlayerImpl(Player player) {
        super(player);
        this.offlinePlayer = null;
        this.player = player;
    }

    @Override
    public boolean comparePlayer(String name) {
        return player.getName().equals(name);
    }

    @Override
    public boolean comparePlayer(UUID uuid) {
        return player.getUniqueId().compareTo(uuid) <= 0;
    }

    @Override
    public boolean comparePlayer(Player player) {
        return this.comparePlayer(player.getUniqueId());
    }

    @Override
    public boolean comparePlayer(OfflinePlayer player) {
        return this.comparePlayer(player.getUniqueId());
    }

    @Override
    public boolean comparePlayer(A_Player player) {
        return this.comparePlayer(player.getUniqueId());
    }

    @Override
    public boolean comparePlayer(A_OfflinePlayer player) {
        return this.comparePlayer(player.getUniqueId());
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return player.getDisplayName();
    }

    @Override
    public void setDisplayName(@Nullable String s) {
        player.setDisplayName(s);
    }

    @Override
    @NotNull
    public String getPlayerListName() {
        return player.getPlayerListName();
    }

    @Override
    public void setPlayerListName(@Nullable String s) {
        player.setPlayerListName(s);
    }

    @Override
    @Nullable
    public String getPlayerListHeader() {
        return player.getPlayerListHeader();
    }

    @Override
    @Nullable
    public String getPlayerListFooter() {
        return player.getPlayerListFooter();
    }

    @Override
    public void setPlayerListHeader(@Nullable String s) {
        player.setPlayerListHeader(s);
    }

    @Override
    public void setPlayerListFooter(@Nullable String s) {
        player.setPlayerListFooter(s);
    }

    @Override
    public void setPlayerListHeaderFooter(@Nullable String s, @Nullable String s1) {
        player.setPlayerListHeaderFooter(s, s1);
    }

    @Override
    public void setCompassTarget(@NotNull Location location) {
        player.setCompassTarget(location);
    }

    @Override
    @NotNull
    public Location getCompassTarget() {
        return player.getCompassTarget();
    }

    @Override
    @Nullable
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    @Override
    public void sendRawMessage(@NotNull String s) {
        player.sendRawMessage(s);
    }

    @Override
    public void kickPlayer(@Nullable String s) {
        player.kickPlayer(s);
    }

    @Override
    public void chat(@NotNull String s) {
        player.chat(s);
    }

    @Override
    public boolean performCommand(@NotNull String s) {
        return player.performCommand(s);
    }

    @Override
    @Deprecated
    public boolean isOnGround() {
        return player.isOnGround();
    }

    @Override
    public boolean isSneaking() {
        return player.isSneaking();
    }

    @Override
    public void setSneaking(boolean b) {
        player.setSneaking(b);
    }

    @Override
    public boolean isSprinting() {
        return player.isSprinting();
    }

    @Override
    public void setSprinting(boolean b) {
        player.setSprinting(b);
    }

    @Override
    public void saveData() {
        player.saveData();
    }

    @Override
    public void loadData() {
        player.loadData();
    }

    @Override
    public void setSleepingIgnored(boolean b) {
        player.setSleepingIgnored(b);
    }

    @Override
    public boolean isSleepingIgnored() {
        return player.isSleepingIgnored();
    }

    @Override
    @Nullable
    public Location getBedSpawnLocation() {
        return player.getBedSpawnLocation();
    }

    @Override
    public void setBedSpawnLocation(@Nullable Location location) {
        player.setBedSpawnLocation(location);
    }

    @Override
    public void setBedSpawnLocation(@Nullable Location location, boolean b) {
        player.setBedSpawnLocation(location, b);
    }

    @Override
    @Deprecated
    public void playNote(@NotNull Location location, byte b, byte b1) {
        player.playNote(location, b, b1);
    }

    @Override
    public void playNote(@NotNull Location location, @NotNull Instrument instrument, @NotNull Note note) {
        player.playNote(location, instrument, note);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float v, float v1) {
        player.playSound(location, sound, v, v1);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String s, float v, float v1) {
        player.playSound(location, s, v, v1);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {
        player.playSound(location, sound, soundCategory, v, v1);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v, float v1) {
        player.playSound(location, s, soundCategory, v, v1);
    }

    @Override
    public void stopSound(@NotNull Sound sound) {
        player.stopSound(sound);
    }

    @Override
    public void stopSound(@NotNull String s) {
        player.stopSound(s);
    }

    @Override
    public void stopSound(@NotNull Sound sound, @Nullable SoundCategory soundCategory) {
        player.stopSound(sound, soundCategory);
    }

    @Override
    public void stopSound(@NotNull String s, @Nullable SoundCategory soundCategory) {
        player.stopSound(s, soundCategory);
    }

    @Override
    public void stopAllSounds() {
        player.stopAllSounds();
    }

    @Override
    @Deprecated
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int i) {
        player.playEffect(location, effect, i);
    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t) {
        player.playEffect(location, effect, t);
    }

    @Override
    public boolean breakBlock(@NotNull Block block) {
        return player.breakBlock(block);
    }

    @Override
    @Deprecated
    public void sendBlockChange(@NotNull Location location, @NotNull Material material, byte b) {
        player.sendBlockChange(location, material, b);
    }

    @Override
    public void sendBlockChange(@NotNull Location location, @NotNull BlockData blockData) {
        player.sendBlockChange(location, blockData);
    }

    @Override
    public void sendBlockDamage(@NotNull Location location, float v) {
        player.sendBlockDamage(location, v);
    }

    @Override
    @Deprecated
    public boolean sendChunkChange(@NotNull Location location, int i, int i1, int i2, @NotNull byte[] bytes) {
        return player.sendChunkChange(location, i, i1, i2, bytes);
    }

    @Override
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings) throws IllegalArgumentException {
        player.sendSignChange(location, strings);
    }

    @Override
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
        player.sendSignChange(location, strings, dyeColor);
    }

    @Override
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {
        player.sendSignChange(location, strings, dyeColor, b);
    }

    @Override
    public void addPlayerRunnable(NamespacedKey key, Runnable runnable, int delay) {

    }

    @Override
    public void removePlayerRunnable(NamespacedKey key) {

    }

    @Override
    public boolean hasPlayerRunnable(NamespacedKey key) {
        return false;
    }


    public void hideEntity(Entity entity) {

    }

    @Override
    public boolean isHiddenEntity(Entity entity) {
        return false;
    }

    @Override
    public void showEntity(Entity entity) {

    }

    @Override
    public void setSkin(String skin, String signature) {

    }

    @Override
    public void setSkin(OfflinePlayer player) {

    }

    @Override
    public boolean isChangedSkin() {
        return false;
    }

    @Override
    public void resetSkin() {

    }

    @Override
    public void delayOpenInventory(Inventory inv) {
        this.delayOpenInventory(inv, 1);
    }

    @Override
    public void delayOpenInventory(Inventory inv, int delay) {
        Bukkit.getScheduler().runTaskLater(CommediaDellartePlugin.instance, () -> openInventory(inv), delay);
    }

    @Override
    public void delayOpenInventory(CustomGui inv) {
        this.delayOpenInventory(inv.getInventory(), 1);
    }

    @Override
    public void delayOpenInventory(CustomGui inv, int delay) {
        this.delayOpenInventory(inv.getInventory(), delay);
    }

    @Override
    public void delayOpenInventory(Inventory inv, boolean ignoreInvCloseEvent) {
        this.setPlayerStatus(PlayerStatus.IgnoreInvClose, ignoreInvCloseEvent);
        this.delayOpenInventory(inv);
    }

    @Override
    public void delayOpenInventory(Inventory inv, int delay, boolean ignoreInvCloseEvent) {
        this.setPlayerStatus(PlayerStatus.IgnoreInvClose, ignoreInvCloseEvent);
        this.delayOpenInventory(inv, delay);
    }

    @Override
    public void delayOpenInventory(CustomGui inv, boolean ignoreInvCloseEvent) {
        this.setPlayerStatus(PlayerStatus.IgnoreInvClose, ignoreInvCloseEvent);
        this.delayOpenInventory(inv);
    }

    @Override
    public void delayOpenInventory(CustomGui inv, int delay, boolean ignoreInvCloseEvent) {
        this.setPlayerStatus(PlayerStatus.IgnoreInvClose, ignoreInvCloseEvent);
        this.delayOpenInventory(inv, delay);
    }

    @Override
    public BlockState lastBreakBlock() {
        return null;
    }

    @Override
    public BlockState lastPlaceBlock() {
        return null;
    }

    @Override
    public void sendActionBar(@NotNull String var1) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(var1));
    }


    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) {
        return this.player.getInventory().addItem(itemStacks);
    }

    @Override
    public void addItemNature(ItemStack... itemStacks) {
        HashMap<Integer, ItemStack> map = this.addItem(itemStacks);
        map.forEach((key, value) -> {
            ItemStack dropItem = value.clone();
            dropItem.setAmount(key);
            this.getWorld().dropItem(player.getLocation(), dropItem);
        });
    }

    @Override
    public void addItemNature(ItemStack itemStack, int amount) {
        ItemStack clone = itemStack.clone();
        clone.setAmount(amount);
        this.addItemNature(itemStack);
    }

    @Override
    public boolean isNPC() {
        return false;
    }

    @Override
    public A_OfflinePlayer getAOfflinePlayer() {
        return this.offlinePlayer;
    }

    @Override
    public void sendMap(@NotNull MapView mapView) {
        player.sendMap(mapView);
    }

    @Override
    public void updateInventory() {
        player.updateInventory();
    }

    @Override
    public void setPlayerTime(long l, boolean b) {
        player.setPlayerTime(l, b);
    }

    @Override
    public long getPlayerTime() {
        return player.getPlayerTime();
    }

    @Override
    public long getPlayerTimeOffset() {
        return player.getPlayerTimeOffset();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return player.isPlayerTimeRelative();
    }

    @Override
    public void resetPlayerTime() {
        player.resetPlayerTime();
    }

    @Override
    public void setPlayerWeather(@NotNull WeatherType weatherType) {
        player.setPlayerWeather(weatherType);
    }

    @Override
    @Nullable
    public WeatherType getPlayerWeather() {
        return player.getPlayerWeather();
    }

    @Override
    public void resetPlayerWeather() {
        player.resetPlayerWeather();
    }

    @Override
    public void giveExp(int i) {
        player.giveExp(i);
    }

    @Override
    public void giveExpLevels(int i) {
        player.giveExpLevels(i);
    }

    @Override
    public float getExp() {
        return player.getExp();
    }

    @Override
    public void setExp(float v) {
        player.setExp(v);
    }

    @Override
    public int getLevel() {
        return player.getLevel();
    }

    @Override
    public void setLevel(int i) {
        player.setLevel(i);
    }

    @Override
    public int getTotalExperience() {
        return player.getTotalExperience();
    }

    @Override
    public void setTotalExperience(int i) {
        player.setTotalExperience(i);
    }

    @Override
    public void sendExperienceChange(float v) {
        player.sendExperienceChange(v);
    }

    @Override
    public void sendExperienceChange(float v, int i) {
        player.sendExperienceChange(v, i);
    }

    @Override
    public boolean getAllowFlight() {
        return player.getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean b) {
        player.setAllowFlight(b);
    }

    @Override
    @Deprecated
    public void hidePlayer(@NotNull Player player) {
        this.player.hidePlayer(player);
    }

    @Override
    public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
        this.player.hidePlayer(plugin, player);
    }

    @Override
    @Deprecated
    public void showPlayer(@NotNull Player player) {
        this.player.showPlayer(player);
    }

    @Override
    public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
        this.player.showPlayer(plugin, player);
    }

    @Override
    public boolean canSee(@NotNull Player player) {
        return this.player.canSee(player);
    }

    @Override
    public boolean isFlying() {
        return player.isFlying();
    }

    @Override
    public void setFlying(boolean b) {
        player.setFlying(b);
    }

    @Override
    public void setFlySpeed(float v) throws IllegalArgumentException {
        player.setFlySpeed(v);
    }

    @Override
    public void setFlySpeed(double var1) throws IllegalArgumentException {
        player.setFlySpeed((float) var1);
    }

    @Override
    public void setWalkSpeed(float v) throws IllegalArgumentException {
        player.setWalkSpeed(v);
    }

    @Override
    public void setWalkSpeed(double var1) throws IllegalArgumentException {
        player.setWalkSpeed((float) var1);
    }

    @Override
    public float getFlySpeed() {
        return player.getFlySpeed();
    }

    @Override
    public float getWalkSpeed() {
        return player.getWalkSpeed();
    }

    @Override
    @Deprecated
    public void setTexturePack(@NotNull String s) {
        player.setTexturePack(s);
    }

    @Override
    public void setResourcePack(@NotNull String s) {
        player.setResourcePack(s);
    }

    @Override
    public void setResourcePack(@NotNull String s, @NotNull byte[] bytes) {
        player.setResourcePack(s, bytes);
    }

    @Override
    @NotNull
    public Scoreboard getScoreboard() {
        return player.getScoreboard();
    }

    @Override
    public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        player.setScoreboard(scoreboard);
    }

    @Override
    public boolean isHealthScaled() {
        return player.isHealthScaled();
    }

    @Override
    public void setHealthScaled(boolean b) {
        player.setHealthScaled(b);
    }

    @Override
    public void setHealthScale(double v) throws IllegalArgumentException {
        player.setHealthScale(v);
    }

    @Override
    public double getHealthScale() {
        return player.getHealthScale();
    }

    @Override
    @Nullable
    public Entity getSpectatorTarget() {
        return player.getSpectatorTarget();
    }

    @Override
    public void setSpectatorTarget(@Nullable Entity entity) {
        player.setSpectatorTarget(entity);
    }

    @Override
    @Deprecated
    public void sendTitle(@Nullable String s, @Nullable String s1) {
        player.sendTitle(s, s1);
    }

    @Override
    public void sendTitle(@Nullable String s, @Nullable String s1, int i, int i1, int i2) {
        player.sendTitle(s, s1, i, i1, i2);
    }

    @Override
    public void resetTitle() {
        player.resetTitle();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i) {
        player.spawnParticle(particle, location, i);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i) {
        player.spawnParticle(particle, v, v1, v2, i);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, @Nullable T t) {
        player.spawnParticle(particle, location, i, t);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
        player.spawnParticle(particle, v, v1, v2, i, t);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2) {
        player.spawnParticle(particle, location, i, v, v1, v2);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, @Nullable T t) {
        player.spawnParticle(particle, location, i, v, v1, v2, t);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, @Nullable T t) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, double v3) {
        player.spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, double v3, @Nullable T t) {
        player.spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, @Nullable T t) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
    }

    @Override
    @NotNull
    public AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
        return player.getAdvancementProgress(advancement);
    }

    @Override
    public int getClientViewDistance() {
        return player.getClientViewDistance();
    }

    @Override
    public int getPing() {
        return player.getPing();
    }

    @Override
    @NotNull
    public String getLocale() {
        return player.getLocale();
    }

    @Override
    public void updateCommands() {
        player.updateCommands();
    }

    @Override
    public void openBook(@NotNull ItemStack itemStack) {
        player.openBook(itemStack);
    }

    @Override
    @NotNull
    public Player.Spigot spigot() {
        return player.spigot();
    }

    @Override
    public boolean getPlayerStatus(PlayerStatus status) {
        return this.statusMap.getOrDefault(status, false);
    }

    @Override
    public void switchPlayerStatus(PlayerStatus status) {
        this.setPlayerStatus(status, !this.getPlayerStatus(status));
    }

    @Override
    public void setPlayerStatus(PlayerStatus status, boolean bool) {
        this.statusMap.put(status, bool);
    }

    @Override
    public void setPlayerChatRunnable(PlayerChatRunnable runnable, NamespacedKey key) {
        this.setPlayerChatRunnable(runnable, key, true);
    }

    @Override
    public void setPlayerChatRunnable(PlayerChatRunnable runnable, NamespacedKey key, boolean sync) {
        this.setPlayerStatus(PlayerStatus.ChatEvent, true);
        this.chatRunMap.put(key, new PairData<>(runnable, sync));
    }

    public void runPlayerChatRunnable(AsyncPlayerChatEvent event) {
        this.chatRunMap.values().forEach(pairData -> {
            PlayerChatRunnable runnable = pairData.dataA();
            boolean sync = pairData.dataB();
            boolean eventSync = event.isAsynchronous();

            if (sync == eventSync) {
                runnable.run(event);
            } else if (sync) {
                Bukkit.getScheduler().runTask(CommediaDellartePlugin.instance, () -> runnable.run(event));
            } else {
                Bukkit.getScheduler().runTaskAsynchronously(CommediaDellartePlugin.instance, () -> runnable.run(event));
            }
        });

        this.chatRunMap.clear();
    }

    @Override
    @NotNull
    public String getName() {
        return player.getName();
    }

    @Override
    @NotNull
    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    @Override
    @NotNull
    public Inventory getEnderChest() {
        return player.getEnderChest();
    }

    @Override
    @NotNull
    public MainHand getMainHand() {
        return player.getMainHand();
    }

    @Override
    public boolean setWindowProperty(@NotNull InventoryView.Property property, int i) {
        return player.setWindowProperty(property, i);
    }

    @Override
    @NotNull
    public InventoryView getOpenInventory() {
        return player.getOpenInventory();
    }

    @Override
    @Nullable
    public InventoryView openInventory(@NotNull Inventory inventory) {
        return player.openInventory(inventory);
    }

    @Override
    public @Nullable InventoryView openInventory(@NotNull Inventory var1, boolean ignoreInvCloseEvent) {
        this.setPlayerStatus(PlayerStatus.IgnoreInvClose, ignoreInvCloseEvent);
        return player.openInventory(var1);
    }

    @Override
    public @Nullable InventoryView openInventory(@NotNull CustomGui var1) {
        return player.openInventory(var1.getInventory());
    }

    @Override
    public @Nullable InventoryView openInventory(@NotNull CustomGui var1, boolean ignoreInvCloseEvent) {
        this.setPlayerStatus(PlayerStatus.IgnoreInvClose, ignoreInvCloseEvent);
        return player.openInventory(var1.getInventory());
    }

    @Override
    @Nullable
    public InventoryView openWorkbench(@Nullable Location location, boolean b) {
        return player.openWorkbench(location, b);
    }

    @Override
    @Nullable
    public InventoryView openEnchanting(@Nullable Location location, boolean b) {
        return player.openEnchanting(location, b);
    }

    @Override
    public void openInventory(@NotNull InventoryView inventoryView) {
        player.openInventory(inventoryView);
    }

    @Override
    @Nullable
    public InventoryView openMerchant(@NotNull Villager villager, boolean b) {
        return player.openMerchant(villager, b);
    }

    @Override
    @Nullable
    public InventoryView openMerchant(@NotNull Merchant merchant, boolean b) {
        return player.openMerchant(merchant, b);
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public void closeInventory(boolean ignoreInvCloseEvent) {
        this.setPlayerStatus(PlayerStatus.IgnoreInvClose, ignoreInvCloseEvent);
        this.closeInventory();
    }

    @Override
    @NotNull
    @Deprecated
    public ItemStack getItemInHand() {
        return player.getItemInHand();
    }

    @Override
    @Deprecated
    public void setItemInHand(@Nullable ItemStack itemStack) {
        player.setItemInHand(itemStack);
    }

    @Override
    @NotNull
    public ItemStack getItemOnCursor() {
        return player.getItemOnCursor();
    }

    @Override
    public void setItemOnCursor(@Nullable ItemStack itemStack) {
        player.setItemOnCursor(itemStack);
    }

    @Override
    public boolean hasCooldown(@NotNull Material material) {
        return player.hasCooldown(material);
    }

    @Override
    public int getCooldown(@NotNull Material material) {
        return player.getCooldown(material);
    }

    @Override
    public void setCooldown(@NotNull Material material, int i) {
        player.setCooldown(material, i);
    }

    @Override
    public int getSleepTicks() {
        return player.getSleepTicks();
    }

    @Override
    public boolean sleep(@NotNull Location location, boolean b) {
        return player.sleep(location, b);
    }

    @Override
    public void wakeup(boolean b) {
        player.wakeup(b);
    }

    @Override
    @NotNull
    public Location getBedLocation() {
        return player.getBedLocation();
    }

    @Override
    @NotNull
    public GameMode getGameMode() {
        return player.getGameMode();
    }

    @Override
    public void setGameMode(@NotNull GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    @Override
    public boolean isBlocking() {
        return player.isBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return player.isHandRaised();
    }

    @Override
    @Nullable
    public ItemStack getItemInUse() {
        return player.getItemInUse();
    }

    @Override
    public int getExpToLevel() {
        return player.getExpToLevel();
    }

    @Override
    public float getAttackCooldown() {
        return player.getAttackCooldown();
    }

    @Override
    public boolean discoverRecipe(@NotNull NamespacedKey namespacedKey) {
        return player.discoverRecipe(namespacedKey);
    }

    @Override
    public int discoverRecipes(@NotNull Collection<NamespacedKey> collection) {
        return player.discoverRecipes(collection);
    }

    @Override
    public boolean undiscoverRecipe(@NotNull NamespacedKey namespacedKey) {
        return player.undiscoverRecipe(namespacedKey);
    }

    @Override
    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> collection) {
        return player.undiscoverRecipes(collection);
    }

    @Override
    public boolean hasDiscoveredRecipe(@NotNull NamespacedKey namespacedKey) {
        return player.hasDiscoveredRecipe(namespacedKey);
    }

    @Override
    @NotNull
    public Set<NamespacedKey> getDiscoveredRecipes() {
        return player.getDiscoveredRecipes();
    }

    @Override
    @Nullable
    @Deprecated
    public Entity getShoulderEntityLeft() {
        return player.getShoulderEntityLeft();
    }

    @Override
    @Deprecated
    public void setShoulderEntityLeft(@Nullable Entity entity) {
        player.setShoulderEntityLeft(entity);
    }

    @Override
    @Nullable
    @Deprecated
    public Entity getShoulderEntityRight() {
        return player.getShoulderEntityRight();
    }

    @Override
    @Deprecated
    public void setShoulderEntityRight(@Nullable Entity entity) {
        player.setShoulderEntityRight(entity);
    }

    @Override
    public boolean dropItem(boolean b) {
        return player.dropItem(b);
    }

    @Override
    public float getExhaustion() {
        return player.getExhaustion();
    }

    @Override
    public void setExhaustion(float v) {
        player.setExhaustion(v);
    }

    @Override
    public float getSaturation() {
        return player.getSaturation();
    }

    @Override
    public void setSaturation(float v) {
        player.setSaturation(v);
    }

    @Override
    public int getFoodLevel() {
        return player.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int i) {
        player.setFoodLevel(i);
    }

    @Override
    public int getSaturatedRegenRate() {
        return player.getSaturatedRegenRate();
    }

    @Override
    public void setSaturatedRegenRate(int i) {
        player.setSaturatedRegenRate(i);
    }

    @Override
    public int getUnsaturatedRegenRate() {
        return player.getUnsaturatedRegenRate();
    }

    @Override
    public void setUnsaturatedRegenRate(int i) {
        player.setUnsaturatedRegenRate(i);
    }

    @Override
    public int getStarvationRate() {
        return player.getStarvationRate();
    }

    @Override
    public void setStarvationRate(int i) {
        player.setStarvationRate(i);
    }

    @Override
    public double getEyeHeight() {
        return player.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean b) {
        return player.getEyeHeight(b);
    }

    @Override
    @NotNull
    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    @Override
    @NotNull
    public List<Block> getLineOfSight(@Nullable Set<Material> set, int i) {
        return player.getLineOfSight(set, i);
    }

    @Override
    @NotNull
    public Block getTargetBlock(@Nullable Set<Material> set, int i) {
        return player.getTargetBlock(set, i);
    }

    @Override
    @NotNull
    public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> set, int i) {
        return player.getLastTwoTargetBlocks(set, i);
    }

    @Override
    @Nullable
    public Block getTargetBlockExact(int i) {
        return player.getTargetBlockExact(i);
    }

    @Override
    @Nullable
    public Block getTargetBlockExact(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
        return player.getTargetBlockExact(i, fluidCollisionMode);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceBlocks(double v) {
        return player.rayTraceBlocks(v);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceBlocks(double v, @NotNull FluidCollisionMode fluidCollisionMode) {
        return player.rayTraceBlocks(v, fluidCollisionMode);
    }

    @Override
    public int getRemainingAir() {
        return player.getRemainingAir();
    }

    @Override
    public void setRemainingAir(int i) {
        player.setRemainingAir(i);
    }

    @Override
    public int getMaximumAir() {
        return player.getMaximumAir();
    }

    @Override
    public void setMaximumAir(int i) {
        player.setMaximumAir(i);
    }

    @Override
    public int getArrowCooldown() {
        return player.getArrowCooldown();
    }

    @Override
    public void setArrowCooldown(int i) {
        player.setArrowCooldown(i);
    }

    @Override
    public int getArrowsInBody() {
        return player.getArrowsInBody();
    }

    @Override
    public void setArrowsInBody(int i) {
        player.setArrowsInBody(i);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return player.getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int i) {
        player.setMaximumNoDamageTicks(i);
    }

    @Override
    public double getLastDamage() {
        return player.getLastDamage();
    }

    @Override
    public void setLastDamage(double v) {
        player.setLastDamage(v);
    }

    @Override
    public int getNoDamageTicks() {
        return player.getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int i) {
        player.setNoDamageTicks(i);
    }

    @Override
    @Nullable
    public Player getKiller() {
        return player.getKiller();
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect potionEffect) {
        return player.addPotionEffect(potionEffect);
    }

    @Override
    @Deprecated
    public boolean addPotionEffect(@NotNull PotionEffect potionEffect, boolean b) {
        return player.addPotionEffect(potionEffect, b);
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection<PotionEffect> collection) {
        return player.addPotionEffects(collection);
    }

    @Override
    public boolean hasPotionEffect(@NotNull PotionEffectType potionEffectType) {
        return player.hasPotionEffect(potionEffectType);
    }

    @Override
    @Nullable
    public PotionEffect getPotionEffect(@NotNull PotionEffectType potionEffectType) {
        return player.getPotionEffect(potionEffectType);
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    @Override
    @NotNull
    public Collection<PotionEffect> getActivePotionEffects() {
        return player.getActivePotionEffects();
    }

    @Override
    public boolean hasLineOfSight(@NotNull Entity entity) {
        return player.hasLineOfSight(entity);
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return player.getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean b) {
        player.setRemoveWhenFarAway(b);
    }

    @Override
    @Nullable
    public EntityEquipment getEquipment() {
        return player.getEquipment();
    }

    @Override
    public void setCanPickupItems(boolean b) {
        player.setCanPickupItems(b);
    }

    @Override
    public boolean getCanPickupItems() {
        return player.getCanPickupItems();
    }

    @Override
    public boolean isLeashed() {
        return player.isLeashed();
    }

    @Override
    @NotNull
    public Entity getLeashHolder() throws IllegalStateException {
        return player.getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(@Nullable Entity entity) {
        return player.setLeashHolder(entity);
    }

    @Override
    public boolean isGliding() {
        return player.isGliding();
    }

    @Override
    public void setGliding(boolean b) {
        player.setGliding(b);
    }

    @Override
    public boolean isSwimming() {
        return player.isSwimming();
    }

    @Override
    public void setSwimming(boolean b) {
        player.setSwimming(b);
    }

    @Override
    public boolean isRiptiding() {
        return player.isRiptiding();
    }

    @Override
    public boolean isSleeping() {
        return player.isSleeping();
    }

    @Override
    public boolean isClimbing() {
        return player.isClimbing();
    }

    @Override
    public void setAI(boolean b) {
        player.setAI(b);
    }

    @Override
    public boolean hasAI() {
        return player.hasAI();
    }

    @Override
    public void attack(@NotNull Entity entity) {
        player.attack(entity);
    }

    @Override
    public void swingMainHand() {
        player.swingMainHand();
    }

    @Override
    public void swingOffHand() {
        player.swingOffHand();
    }

    @Override
    public void setCollidable(boolean b) {
        player.setCollidable(b);
    }

    @Override
    public boolean isCollidable() {
        return player.isCollidable();
    }

    @Override
    @NotNull
    public Set<UUID> getCollidableExemptions() {
        return player.getCollidableExemptions();
    }

    @Override
    @Nullable
    public <T> T getMemory(@NotNull MemoryKey<T> memoryKey) {
        return player.getMemory(memoryKey);
    }

    @Override
    public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T t) {
        player.setMemory(memoryKey, t);
    }

    @Override
    @NotNull
    public EntityCategory getCategory() {
        return player.getCategory();
    }

    @Override
    public void setInvisible(boolean b) {
        player.setInvisible(b);
    }

    @Override
    public boolean isInvisible() {
        return player.isInvisible();
    }

    @Override
    @Nullable
    public AttributeInstance getAttribute(@NotNull Attribute attribute) {
        return player.getAttribute(attribute);
    }

    @Override
    public void damage(double v) {
        player.damage(v);
    }

    @Override
    public void damage(double v, @Nullable Entity entity) {
        player.damage(v, entity);
    }

    @Override
    public double getHealth() {
        return player.getHealth();
    }

    @Override
    public void setHealth(double v) {
        player.setHealth(v);
    }

    @Override
    public double getAbsorptionAmount() {
        return player.getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double v) {
        player.setAbsorptionAmount(v);
    }

    @Override
    @Deprecated
    public double getMaxHealth() {
        return player.getMaxHealth();
    }

    @Override
    @Deprecated
    public void setMaxHealth(double v) {
        player.setMaxHealth(v);
    }

    @Override
    @Deprecated
    public void resetMaxHealth() {
        player.resetMaxHealth();
    }

    @Override
    @NotNull
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    @Nullable
    @Contract("null -> null; !null -> !null")
    public Location getLocation(@Nullable Location location) {
        return player.getLocation(location);
    }

    @Override
    public void setVelocity(@NotNull Vector vector) {
        player.setVelocity(vector);
    }

    @Override
    @NotNull
    public Vector getVelocity() {
        return player.getVelocity();
    }

    @Override
    public double getHeight() {
        return player.getHeight();
    }

    @Override
    public double getWidth() {
        return player.getWidth();
    }

    @Override
    @NotNull
    public BoundingBox getBoundingBox() {
        return player.getBoundingBox();
    }

    @Override
    public boolean isInWater() {
        return player.isInWater();
    }

    @Override
    @NotNull
    public World getWorld() {
        return player.getWorld();
    }

    @Override
    public void setRotation(float v, float v1) {
        player.setRotation(v, v1);
    }

    @Override
    public boolean teleport(@NotNull Location location) {
        return player.teleport(location);
    }

    @Override
    public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(location, teleportCause);
    }

    @Override
    public boolean teleport(@NotNull Entity entity) {
        return player.teleport(entity);
    }

    @Override
    public boolean teleport(@NotNull Entity entity, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
        return player.teleport(entity, teleportCause);
    }

    @Override
    @NotNull
    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return player.getNearbyEntities(v, v1, v2);
    }

    @Override
    public int getEntityId() {
        return player.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return player.getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return player.getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int i) {
        player.setFireTicks(i);
    }

    @Override
    public void setVisualFire(boolean b) {
        player.setVisualFire(b);
    }

    @Override
    public boolean isVisualFire() {
        return player.isVisualFire();
    }

    @Override
    public int getFreezeTicks() {
        return player.getFreezeTicks();
    }

    @Override
    public int getMaxFreezeTicks() {
        return player.getMaxFreezeTicks();
    }

    @Override
    public void setFreezeTicks(int i) {
        player.setFreezeTicks(i);
    }

    @Override
    public boolean isFrozen() {
        return player.isFrozen();
    }

    @Override
    public void remove() {
        player.remove();
    }

    @Override
    public boolean isDead() {
        return player.isDead();
    }

    @Override
    public boolean isValid() {
        return player.isValid();
    }

    @Override
    @NotNull
    public Server getServer() {
        return player.getServer();
    }

    @Override
    public boolean isPersistent() {
        return player.isPersistent();
    }

    @Override
    public void setPersistent(boolean b) {
        player.setPersistent(b);
    }

    @Override
    @Nullable
    @Deprecated
    public Entity getPassenger() {
        return player.getPassenger();
    }

    @Override
    @Deprecated
    public boolean setPassenger(@NotNull Entity entity) {
        return player.setPassenger(entity);
    }

    @Override
    @NotNull
    public List<Entity> getPassengers() {
        return player.getPassengers();
    }

    @Override
    public boolean addPassenger(@NotNull Entity entity) {
        return player.addPassenger(entity);
    }

    @Override
    public boolean removePassenger(@NotNull Entity entity) {
        return player.removePassenger(entity);
    }

    @Override
    public boolean isEmpty() {
        return player.isEmpty();
    }

    @Override
    public boolean eject() {
        return player.eject();
    }

    @Override
    public float getFallDistance() {
        return player.getFallDistance();
    }

    @Override
    public void setFallDistance(float v) {
        player.setFallDistance(v);
    }

    @Override
    public void setLastDamageCause(@Nullable EntityDamageEvent entityDamageEvent) {
        player.setLastDamageCause(entityDamageEvent);
    }

    @Override
    @Nullable
    public EntityDamageEvent getLastDamageCause() {
        return player.getLastDamageCause();
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return player.getTicksLived();
    }

    @Override
    public void setTicksLived(int i) {
        player.setTicksLived(i);
    }

    @Override
    public void playEffect(@NotNull EntityEffect entityEffect) {
        player.playEffect(entityEffect);
    }

    @Override
    @NotNull
    public EntityType getType() {
        return player.getType();
    }

    @Override
    public boolean isInsideVehicle() {
        return player.isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return player.leaveVehicle();
    }

    @Override
    @Nullable
    public Entity getVehicle() {
        return player.getVehicle();
    }

    @Override
    public void setCustomNameVisible(boolean b) {
        player.setCustomNameVisible(b);
    }

    @Override
    public boolean isCustomNameVisible() {
        return player.isCustomNameVisible();
    }

    @Override
    public void setGlowing(boolean b) {
        player.setGlowing(b);
    }

    @Override
    public boolean isGlowing() {
        return player.isGlowing();
    }

    @Override
    public void setInvulnerable(boolean b) {
        player.setInvulnerable(b);
    }

    @Override
    public boolean isInvulnerable() {
        return player.isInvulnerable();
    }

    @Override
    public boolean isSilent() {
        return player.isSilent();
    }

    @Override
    public void setSilent(boolean b) {
        player.setSilent(b);
    }

    @Override
    public boolean hasGravity() {
        return player.hasGravity();
    }

    @Override
    public void setGravity(boolean b) {
        player.setGravity(b);
    }

    @Override
    public int getPortalCooldown() {
        return player.getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int i) {
        player.setPortalCooldown(i);
    }

    @Override
    @NotNull
    public Set<String> getScoreboardTags() {
        return player.getScoreboardTags();
    }

    @Override
    public boolean addScoreboardTag(@NotNull String s) {
        return player.addScoreboardTag(s);
    }

    @Override
    public boolean removeScoreboardTag(@NotNull String s) {
        return player.removeScoreboardTag(s);
    }

    @Override
    @NotNull
    public PistonMoveReaction getPistonMoveReaction() {
        return player.getPistonMoveReaction();
    }

    @Override
    @NotNull
    public BlockFace getFacing() {
        return player.getFacing();
    }

    @Override
    @NotNull
    public Pose getPose() {
        return player.getPose();
    }

    @Override
    public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
        player.setMetadata(s, metadataValue);
    }

    @Override
    @NotNull
    public List<MetadataValue> getMetadata(@NotNull String s) {
        return player.getMetadata(s);
    }

    @Override
    public boolean hasMetadata(@NotNull String s) {
        return player.hasMetadata(s);
    }

    @Override
    public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
        player.removeMetadata(s, plugin);
    }

    @Override
    public void sendMessage(@NotNull String s) {
        player.sendMessage(s);
    }

    @Override
    public void sendMessage(@NotNull String... strings) {
        player.sendMessage(strings);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
        player.sendMessage(uuid, s);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {
        player.sendMessage(uuid, strings);
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return player.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return player.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return player.hasPermission(s);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return player.hasPermission(permission);
    }

    @Override
    @NotNull
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
        return player.addAttachment(plugin, s, b);
    }

    @Override
    @NotNull
    public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return player.addAttachment(plugin);
    }

    @Override
    @Nullable
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        return player.addAttachment(plugin, s, b, i);
    }

    @Override
    @Nullable
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        return player.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
        player.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        player.recalculatePermissions();
    }

    @Override
    @NotNull
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return player.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return player.isOp();
    }

    @Override
    public void setOp(boolean b) {
        player.setOp(b);
    }

    @Override
    @Nullable
    public String getCustomName() {
        return player.getCustomName();
    }

    @Override
    public void setCustomName(@Nullable String s) {
        player.setCustomName(s);
    }

    @Override
    @NotNull
    public PersistentDataContainer getPersistentDataContainer() {
        return player.getPersistentDataContainer();
    }

    @Override
    @NotNull
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> aClass) {
        return player.launchProjectile(aClass);
    }

    @Override
    @NotNull
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> aClass, @Nullable Vector vector) {
        return player.launchProjectile(aClass, vector);
    }

    @Override
    public boolean isConversing() {
        return player.isConversing();
    }

    @Override
    public void acceptConversationInput(@NotNull String s) {
        player.acceptConversationInput(s);
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return player.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        player.abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
        player.abandonConversation(conversation, conversationAbandonedEvent);
    }

    @Override
    public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {
        player.sendRawMessage(uuid, s);
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public boolean isBanned() {
        return player.isBanned();
    }

    @Override
    public boolean isWhitelisted() {
        return player.isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean b) {
        player.setWhitelisted(b);
    }

    @Override
    @Nullable
    public Player getPlayer() {
        return player.getPlayer();
    }

    @Override
    public A_Player getAPlayer() {
        return null;
    }

    @Override
    public long getFirstPlayed() {
        return player.getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return player.getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return player.hasPlayedBefore();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        player.incrementStatistic(statistic);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        player.decrementStatistic(statistic);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, i);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        player.decrementStatistic(statistic, i);
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        player.setStatistic(statistic, i);
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        return player.getStatistic(statistic);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material);
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        return player.getStatistic(statistic, material);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material, i);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material, i);
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        player.setStatistic(statistic, material, i);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        player.decrementStatistic(statistic, entityType);
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        return player.getStatistic(statistic, entityType);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType, i);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
        player.decrementStatistic(statistic, entityType, i);
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
        player.setStatistic(statistic, entityType, i);
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return player.serialize();
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, @NotNull byte[] bytes) {
        player.sendPluginMessage(plugin, s, bytes);
    }

    @Override
    public ItemStack getPlayerSkull() {
        return null;
    }

    @Override
    @NotNull
    public Set<String> getListeningPluginChannels() {
        return player.getListeningPluginChannels();
    }

    @Override
    public Player getEntity() {
        return this.player;
    }
}
