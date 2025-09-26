package org.red;

import org.bukkit.Bukkit;
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
import org.red.data.storage.DataStorage;
import org.red.data.storage.FileStorage;
import org.red.data.storage.MySQLMariaStorage;
import org.red.entity.A_EntityImpl;
import org.red.entity.A_LivingEntityImpl;
import org.red.entity.A_NPCImpl;
import org.red.entity.A_PlayerImpl;
import org.red.library.IDellarteManager;
import org.red.library.entity.A_Entity;
import org.red.library.interactive.InteractiveManager;
import org.red.library.user.A_OfflinePlayer;
import org.red.library.util.A_Data;
import org.red.library.util.BossBarTimer;
import org.red.library.util.Timer;
import org.red.library.util.PluginData;
import org.red.library.world.A_World;
import org.red.user.A_OfflinePlayerImpl;
import org.red.util.A_File;
import org.red.util.A_PluginData;
import org.red.util.A_YamlConfiguration;
import org.red.util.BossBarTimerImpl;
import org.red.util.TimerImpl;
import org.red.world.A_WorldImpl;

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
    private DataStorage storage;
    //private final HashMap<Class<?>, > handlerMap = new HashMap<>();

    private final HashMap<String, PluginData> pluginDataMap = new HashMap<>();

    @Override
    public PluginData getPluginData(Plugin plugin) {
        return pluginDataMap.computeIfAbsent(plugin.getName(), k -> A_PluginData.newPluginData(plugin));
    }

    public void savePluginData(Plugin plugin, String key) {
        PluginData pluginData = pluginDataMap.get(plugin.getName());
        CommediaDellartePlugin.sendDebugLog("Saving Plugin Data for " + plugin.getName());
    }

    public void saveWorldData(A_World world) {
        pluginDataMap.values().forEach(data -> ((A_PluginData) data).saveWorldData(world));  
    }

    public void loadWorldData(A_World world) {
        pluginDataMap.values().forEach(data -> ((A_PluginData) data).loadWorldData(world));  
    }

    public void savePlayerData(A_OfflinePlayer player) {
        pluginDataMap.values().forEach(data -> ((A_PluginData) data).savePlayerData(player));  
    }

    public void loadPlayerData(A_OfflinePlayer player) {
        pluginDataMap.values().forEach(data -> ((A_PluginData) data).loadPlayerData(player));  
    }

    public void allDataSave() {
        offlinePlayers.values().forEach(A_OfflinePlayerImpl::aDataSave);
        CommediaDellartePlugin.sendDebugLog("Saved All Player Data");
        worlds.values().forEach(A_WorldImpl::aDataSave);
        CommediaDellartePlugin.sendDebugLog("Saved All World Data");
        this.entitiesDataSave();
    }

    public void entitiesDataSave() {
        pluginDataMap.values().forEach(data -> ((A_PluginData) data).saveEntitiesData());
    }

    public void entitiesDataLoad() {
        pluginDataMap.values().forEach(data -> ((A_PluginData) data).loadEntitiesData());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
    }

    public DataStorage getStorage() {
        return this.storage;
    }

    public void getStroageLoad() {
        try {
            if (!Config.DATABASE_ENABLED.asBooleanValue()) {
                this.storage = new FileStorage();
                return;
            }

            String type = Config.DATABASE_TYPE.asStringValue();
            String host = Config.DATABASE_HOST.asStringValue();
            String database = Config.DATABASE_NAME.asStringValue();
            int port = Config.DATABASE_PORT.asIntValue();
            String user = Config.DATABASE_USER.asStringValue();
            String password = Config.DATABASE_PASSWORD.asStringValue();

            switch (type) {
                case "mysql", "mariadb" -> storage = new MySQLMariaStorage(host, database, port, user, password, type.equals("mysql"));
                default -> this.storage = new FileStorage();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
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
            A_OfflinePlayerImpl aOfflinePlayer = new A_OfflinePlayerImpl(player, A_Data.newAData());
            aOfflinePlayer.aDataLoad();
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
           aWorld.aDataLoad();
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
}
