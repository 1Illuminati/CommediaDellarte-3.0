package org.red.minecraft.dellarte;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.library.data.adapter.DatabaseAdapter;
import org.red.library.data.adapter.FileAdapter;
import org.red.library.data.adapter.IAdapter;
import org.red.library.data.adapter.MySqlAdapter;
import org.red.library.data.serialize.RegisterSerializable;
import org.red.minecraft.dellarte.data.DataStorage;
import org.red.minecraft.dellarte.data.NoneAdapter;
import org.red.minecraft.dellarte.data.SaveConfig;
import org.red.minecraft.dellarte.entity.A_EntityImpl;
import org.red.minecraft.dellarte.entity.A_LivingEntityImpl;
import org.red.minecraft.dellarte.entity.A_NPCImpl;
import org.red.minecraft.dellarte.entity.A_PlayerImpl;
import org.red.minecraft.dellarte.exception.DataStorageNullException;
import org.red.minecraft.dellarte.library.IDellarteManager;
import org.red.minecraft.dellarte.library.interactive.InteractiveManager;
import org.red.minecraft.dellarte.library.util.BossBarTimer;
import org.red.minecraft.dellarte.library.util.NamespaceMap;
import org.red.minecraft.dellarte.library.util.Timer;
import org.red.minecraft.dellarte.library.world.A_World;
import org.red.minecraft.dellarte.user.A_OfflinePlayerImpl;
import org.red.minecraft.dellarte.util.A_File;
import org.red.minecraft.dellarte.util.BossBarTimerImpl;
import org.red.minecraft.dellarte.util.TimerImpl;
import org.red.minecraft.dellarte.world.A_WorldImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class DellarteManager implements IDellarteManager {
    private final HashMap<UUID, A_NPCImpl> NPCs = new HashMap<>();
    private final HashMap<UUID, A_PlayerImpl> players = new HashMap<>();
    private final HashMap<UUID, A_OfflinePlayerImpl> offlinePlayers = new HashMap<>();
    private final HashMap<UUID, A_WorldImpl> worlds = new HashMap<>();
    private final HashMap<UUID, A_EntityImpl> entities = new HashMap<>(); 
    private final HashMap<Class<?>, InteractiveManager> interactiveManagerHashMap = new HashMap<>();
    //private final HashMap<Class<?>, > handlerMap = new HashMap<>();

    private final NamespaceMap<DataStorage> map = new NamespaceMap<>();
    private final NamespaceMap<BukkitTask> stroageTaskMap = new NamespaceMap<>();

    @Override
    public DataStorage getStorage(NamespacedKey key) {
        if (!map.containsKey(key)) throw new DataStorageNullException(key);

        return map.get(key);
    }

    public void setStorageAutoSave(DataStorage storage) {
        int delay = storage.config().getAutoSaveTime() * 20;
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(CommediaDellartePlugin.instance, () -> {
            storage.saveAll();
        }, 
        delay, delay);

        stroageTaskMap.put(storage.getKey(), task);
    }

    /**
     * 데이터 스토리지을 콘피그에서 불러와 생성하는 함수
     */
    public void createDataStorages() {
        ConfigurationSection section = CommediaDellartePlugin.config.getConfigurationSection("data-storage");

        section.getKeys(false).forEach(key -> {
            ConfigurationSection pluginSection = section.getConfigurationSection(key);
            pluginSection.getKeys(false).forEach(type -> {
                SaveConfig config = SaveConfig.createSaveConfig(new NamespacedKey(key.toLowerCase(), type), pluginSection.getConfigurationSection(key));
                DataStorage storage = new DataStorage(config, createAdapter(config));
                map.put(config.getKey(), storage);
                setStorageAutoSave(storage);
            });

            
            if (!pluginSection.contains("player"))
                createDefaultStorage(key, "player");
            if (!pluginSection.contains("entity"))
                createDefaultStorage(key, "entity");
            if (!pluginSection.contains("world"))
                createDefaultStorage(key, "world");
        });
    }

    public void createDefaultStorage(String key, String type) {
        NamespacedKey nkey = new NamespacedKey(key.toLowerCase(), type);
        map.put(nkey, DataStorage.createDefaultDataStorage(nkey));
    }

    /**
     * 데이터 스토리지 생성에 필요한 어뎁터를 생성하는 함수
     */
    public IAdapter createAdapter(SaveConfig config) {
        IAdapter result = null;

        if (!config.isEnable()) return new NoneAdapter();

        switch(config.getSaveType()) {
            case FILE: result = new FileAdapter(new A_File(config.getKey().getNamespace() + "/" + config.getKey().getKey()));
            case MYSQL: {
                try {
                    result = new MySqlAdapter(getDBConfigLoad(config.getKey()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            };
        };

        return result;
    }

    /**
     * 데이터베이스 어댑터에 필요한 설정을 생성하는 함수
     * 에러가 날 경우 처리는 안되어있다
     */
    public DatabaseAdapter.Config getDBConfigLoad(NamespacedKey key) {
        if (!Config.DATABASE_ENABLED.asBooleanValue()) {
            return null;
        }

        String host = Config.DATABASE_HOST.asStringValue();
        String database = Config.DATABASE_NAME.asStringValue();
        int port = Config.DATABASE_PORT.asIntValue();
        String user = Config.DATABASE_USER.asStringValue();
        String password = Config.DATABASE_PASSWORD.asStringValue();

        return new DatabaseAdapter.Config(host, database, port, user, password, key.toString().replace(":", "_"));
    }

    @Override
    public boolean containStorage(NamespacedKey key) {
        return map.containsKey(key);
    }

    public void allDataSave() {
        map.values().forEach(DataStorage::saveAll);
    }

    public void allDataLoad() {
        map.values().forEach(DataStorage::saveAll);
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
    public <T> void registerSerializableClass(Class<T> clazz, RegisterSerializable<T> registerSerializable) {
        DataStorage.regSerializableClass(clazz, registerSerializable);
    }
}
