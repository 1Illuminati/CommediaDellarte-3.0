package org.red;

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
import org.red.entity.A_EntityImpl;
import org.red.entity.A_LivingEntityImpl;
import org.red.entity.A_NPCImpl;
import org.red.entity.A_PlayerImpl;
import org.red.interactive.InteractiveManagerImpl;
import org.red.library.IDellarteManager;
import org.red.library.entity.A_Entity;
import org.red.library.interactive.InteractiveManager;
import org.red.library.util.A_Data;
import org.red.library.util.BossBarTimer;
import org.red.library.util.Timer;
import org.red.library.world.A_World;
import org.red.user.A_OfflinePlayerImpl;
import org.red.util.A_File;
import org.red.util.A_YamlConfiguration;
import org.red.util.BossBarTimerImpl;
import org.red.util.TimerImpl;
import org.red.world.A_WorldImpl;

import java.util.HashMap;
import java.util.UUID;

public class DellarteManager implements IDellarteManager {
    private final HashMap<UUID, A_NPCImpl> NPCs = new HashMap<>();
    private final HashMap<UUID, A_PlayerImpl> players = new HashMap<>();
    private final HashMap<UUID, A_OfflinePlayerImpl> offlinePlayers = new HashMap<>();
    private final HashMap<UUID, A_WorldImpl> worlds = new HashMap<>();
    private final HashMap<UUID, A_EntityImpl> entities = new HashMap<>();
    private final HashMap<Class<?>, InteractiveManager> interactiveManagerHashMap = new HashMap<>();

    public void allDataSave() {
        offlinePlayers.values().forEach(A_OfflinePlayerImpl::aDataSave);
        CommediaDellartePlugin.sendDebugLog("Saved All Player Data");
        worlds.values().forEach(A_WorldImpl::aDataSave);
        CommediaDellartePlugin.sendDebugLog("Saved All World Data");
        this.entitiesDataSave();
    }

    public void entitiesDataSave() {
        A_YamlConfiguration yamlConfiguration = new A_YamlConfiguration();
        entities.forEach((uuid, aEntity) -> {
            yamlConfiguration.set(uuid.toString(), aEntity.getAData());
        });
        yamlConfiguration.save(new A_File("entities.yml"));
        CommediaDellartePlugin.sendDebugLog("Saved Entities Data");
    }

    public void entitiesDataLoad() {
        A_YamlConfiguration yamlConfiguration = new A_YamlConfiguration();
        yamlConfiguration.load(new A_File("entities.yml"));
        yamlConfiguration.getKeys(false).forEach(key -> {
            UUID uuid = UUID.fromString(key);
            Entity entity = Bukkit.getEntity(uuid);

            if (entity != null) {
                A_Entity aEntity = getAEntity(entity);
                aEntity.getAData().copy(yamlConfiguration.getSerializable(key, A_Data.class));
            }
        });
        CommediaDellartePlugin.sendDebugLog("Loaded Entities Data");
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
                A_NPCImpl aNPC = new A_NPCImpl(player, A_Data.newAData());
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
        return this.entities.computeIfAbsent(entity.getUniqueId(), uuid -> new A_EntityImpl(entity, A_Data.newAData()));
    }

    @Override
    public A_LivingEntityImpl getALivingEntity(@NotNull LivingEntity entity) {
        if (entity instanceof Player player) return getAPlayer(player);
        return (A_LivingEntityImpl) this.entities.computeIfAbsent(entity.getUniqueId(), uuid -> new A_LivingEntityImpl(entity, A_Data.newAData()));
    }

    @Override
    public A_WorldImpl getAWorld(@NotNull World world) {
        return worlds.computeIfAbsent(world.getUID(), uuid -> {
           A_WorldImpl aWorld = new A_WorldImpl(world, A_Data.newAData());
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
