package org.red.minecraft.dellarte;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.compatibility.dataServer.A_DataClient;
import org.red.minecraft.dellarte.data.StorageManager;
import org.red.minecraft.dellarte.data.serialize.A_RegisterSerializable;
import org.red.minecraft.dellarte.entity.A_EntityImpl;
import org.red.minecraft.dellarte.entity.A_LivingEntityImpl;
import org.red.minecraft.dellarte.entity.A_NPCImpl;
import org.red.minecraft.dellarte.entity.A_PlayerImpl;
import org.red.minecraft.dellarte.library.IDellarteManager;
import org.red.minecraft.dellarte.library.data.AdapterFactory;
import org.red.minecraft.dellarte.library.data.IDataStorage;
import org.red.minecraft.dellarte.library.exception.DataStorageNullException;
import org.red.minecraft.dellarte.library.interactive.InteractiveManager;
import org.red.minecraft.dellarte.library.util.BossBarTimer;
import org.red.minecraft.dellarte.library.util.Timer;
import org.red.minecraft.dellarte.library.world.A_World;
import org.red.minecraft.dellarte.user.A_OfflinePlayerImpl;
import org.red.minecraft.dellarte.util.BossBarTimerImpl;
import org.red.minecraft.dellarte.util.TimerImpl;
import org.red.minecraft.dellarte.world.A_WorldImpl;
import org.red.minecraft.dellarte.library.data.serializable.RegisterSerializable;

import java.util.HashMap;
import java.util.UUID;

public class DellarteManager implements IDellarteManager {
    private final HashMap<UUID, A_NPCImpl> NPCs = new HashMap<>();
    private final HashMap<UUID, A_PlayerImpl> players = new HashMap<>();
    private final HashMap<UUID, A_OfflinePlayerImpl> offlinePlayers = new HashMap<>();
    private final HashMap<UUID, A_WorldImpl> worlds = new HashMap<>();
    private final HashMap<UUID, A_EntityImpl> entities = new HashMap<>(); 
    private final HashMap<Class<?>, InteractiveManager> interactiveManagerHashMap = new HashMap<>();
    private final StorageManager storageManager = new StorageManager();
    private A_DataClient client;

    @Override
    @Nullable
    public IDataStorage getStorage(NamespacedKey key) {
        try {
            return this.storageManager.getStorage(key);
        } catch (DataStorageNullException e) {
            return null;
        }
    }

    /**
     * player, entity, world타입을 불러올때만 사용하는 함수
     * 굳이 델라테 콘피그에 등록하지 않아도 편하게 사용하기 위해서 만들었다
     * 단 저장은 되지 않는다
     * 만약 등록되어있을 경우 등록된 스토리지를 반환한다
     * @param key 불러올 스토리지 키
     * @return 스토리지
     */
    public IDataStorage getDefaultTypeStorage(NamespacedKey key) {
        try {
            return getStorage(key);
        } catch (DataStorageNullException exception) {
            return this.storageManager.getTempStorage(key);
        }
    }

    @Override
    public boolean containStorage(NamespacedKey key) {
        return this.storageManager.containStorage(key);
    }

    @Override
    public void registerAdapterFactory(AdapterFactory factory) {
        this.storageManager.setFactory(factory);
    }

    public StorageManager getStorageManager() {
        return this.storageManager;
    }

    public void allDataSave() {
        this.storageManager.allDataSave();
    }

    public void allDataLoad() {
        this.storageManager.allDataLoad();
    }

    @Override
    public @Nullable A_PlayerImpl getAPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        return player == null ? null : getAPlayer(player);
    }

    @Override
    public @Nullable A_PlayerImpl getAPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player == null ? null : getAPlayer(player);
    }

    @Override
    public A_PlayerImpl getAPlayer(@NotNull Player player) {
        if (player.hasMetadata("NPC")) {
            return NPCs.computeIfAbsent(player.getUniqueId(), uuid -> {
                A_NPCImpl aNPC = new A_NPCImpl(player);
                CommediaDellartePlugin.sendDebugLog("Created Npc name: " + player.getName() + " uuid: " + uuid);
                return aNPC;
            });
        }

        return players.computeIfAbsent(player.getUniqueId(), uuid -> {
            A_PlayerImpl aPlayer = new A_PlayerImpl(getAOfflinePlayer(player), player);
            CommediaDellartePlugin.sendDebugLog("Created Player name: " + player.getName() + " uuid: " + uuid);
            return aPlayer;
        });
    }

    @Override
    public @Nullable A_PlayerImpl getAPlayer(@NotNull OfflinePlayer player) {
        if (!player.isOnline()) return null;
        return getAPlayer(player.getPlayer());
    }

    @Override
    public A_OfflinePlayerImpl getAOfflinePlayer(@NotNull OfflinePlayer player) {
        return offlinePlayers.computeIfAbsent(player.getUniqueId(), uuid -> {
            A_OfflinePlayerImpl aOfflinePlayer = new A_OfflinePlayerImpl(player);
            CommediaDellartePlugin.sendDebugLog("Created OfflinePlayer name: " + player.getName() + " uuid: " + uuid);
            return aOfflinePlayer;
        });
    }

    @Override
    public A_EntityImpl getAEntity(@NotNull Entity entity) {
        if (entity instanceof Player player) return getAPlayer(player);
        if (entity instanceof LivingEntity livingEntity) return getALivingEntity(livingEntity);
        return this.entities.computeIfAbsent(entity.getUniqueId(), uuid -> new A_EntityImpl(entity));
    }

    @Override
    public A_LivingEntityImpl getALivingEntity(@NotNull LivingEntity entity) {
        if (entity instanceof Player player) return getAPlayer(player);
        return (A_LivingEntityImpl) this.entities.computeIfAbsent(entity.getUniqueId(), uuid -> new A_LivingEntityImpl(entity));
    }

    @Override
    public A_WorldImpl getAWorld(@NotNull World world) {
        return worlds.computeIfAbsent(world.getUID(), uuid -> {
            A_WorldImpl aWorld = new A_WorldImpl(world);
            CommediaDellartePlugin.sendDebugLog("Created World name: " + world.getName() + " uuid: " + world.getUID());
            return aWorld;
        });
    }

    @Override
    public @Nullable A_World getAWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        return world == null ? null : getAWorld(world);
    }

    @Override
    public @Nullable A_World getAWorld(UUID worldUUID) {
        World world = Bukkit.getWorld(worldUUID);
        return world == null ? null : getAWorld(world);
    }

    @Override
    public <T> InteractiveManager<T> getInteractiveManager(@NotNull Class<T> clazz) {
        if (!this.interactiveManagerHashMap.containsKey(clazz)) throw new NullPointerException("");
        return this.interactiveManagerHashMap.get(clazz);
    }

    @Override
    public <T> boolean setInteractiveManager(@NotNull Class<T> clazz, @NotNull InteractiveManager<T> manager) {
        if (this.interactiveManagerHashMap.containsKey(clazz)) return false;

        this.interactiveManagerHashMap.put(clazz, manager);
        return true;
    }

    @Override
    public Timer createTimer(@NotNull NamespacedKey key, int maxTime, @Nullable Runnable runnable) {
        return new TimerImpl(key, maxTime, runnable);
    }

    @Override
    public BossBarTimer createBossBarTimer(NamespacedKey key, int maxTime, @Nullable Runnable runnable, BossBar... bossBars) {
        return new BossBarTimerImpl(key, maxTime, runnable, bossBars);
    }

    @Override
    public <T> void registerSerializableClass(RegisterSerializable<T> registerSerializable) {
        this.storageManager.registerSerializableClass(registerSerializable.getType(), new A_RegisterSerializable<>(registerSerializable));
        CommediaDellartePlugin.sendDebugLog(String.format("Registered Serializable Type: %s", registerSerializable.getType().getName()));
    }

    public void connectDataServer() {
        CommediaDellartePlugin.sendLog("DataServer Enabled Start DataServer Connection");
        try {
            this.client = new A_DataClient(PluginConfig.DATA_SERVER_HOST.asStringValue(), PluginConfig.DATA_SERVER_PORT.asIntValue());
            this.client.startClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public A_DataClient getClient() {
        return client;
    }
}
