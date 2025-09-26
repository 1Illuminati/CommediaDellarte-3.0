package org.red.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.red.CommediaDellartePlugin;
import org.red.data.storage.DataStorage;
import org.red.library.CommediaDellarte;
import org.red.library.entity.A_Entity;
import org.red.library.entity.A_Player;
import org.red.library.user.A_OfflinePlayer;
import org.red.library.util.CoolTimeMap;
import org.red.library.util.DataMap;
import org.red.library.util.PluginData;
import org.red.library.world.A_World;

public class A_PluginData implements PluginData {
    private final Plugin plugin;
    private final Map<String, DataMap> dataMaps;
    private final Map<String, DataMap> coolTimes;

    private A_PluginData(Plugin plugin, Map<String, DataMap> dataMaps, Map<String, DataMap> coolTimes) {
        this.plugin = plugin;
        this.dataMaps = dataMaps;
        this.coolTimes = coolTimes;
    }

    private void setDataMap(String type, String key, DataMap map) {
        dataMaps.computeIfAbsent(type, k -> new DataMap()).put(key, map);
    }

    private void setCoolTimeMap(String type, String key, CoolTimeMap map) {
        coolTimes.computeIfAbsent(type, k -> new DataMap()).put(key, map);
    }

    public DataMap getDataMap(String type, String key) {
        return dataMaps.computeIfAbsent(type, k -> new DataMap()).getDataMap(key);
    }

    public CoolTimeMap getCoolTimeMap(String type, String key) {
        return coolTimes.computeIfAbsent(type, k -> new DataMap()).getClass(key, CoolTimeMap.class, new CoolTimeMap());
    }

    public DataMap getEntityDataMap(A_Entity entity) {
        if (entity instanceof A_Player player)
            return getPlayerDataMap(player);

        return getDataMap("entity", entity.getUniqueId().toString());
    }

    public CoolTimeMap getEntityCoolTimeMap(A_Entity entity) {
        if (entity instanceof A_Player player)
            return getPlayerCoolTimeMap(player);

        return getCoolTimeMap("entity", entity.getUniqueId().toString());
    }

    public DataMap getWorldDataMap(A_World world) {
        return getWorldDataMap(world.getName());
    }

    public DataMap getWorldDataMap(String worldName) {
        return getDataMap("world", worldName);
    }

    public CoolTimeMap getWorldCoolTimeMap(A_World world) {
        return getWorldCoolTimeMap(world.getName());
    }

    public CoolTimeMap getWorldCoolTimeMap(String worldName) {
        return getCoolTimeMap("world", worldName);
    }

    public DataMap getPlayerDataMap(A_Player player) {
        return getPlayerDataMap(player.getUniqueId());
    }

    public DataMap getPlayerDataMap(A_OfflinePlayer player) {
        return getPlayerDataMap(player.getUniqueId());
    }

    public DataMap getPlayerDataMap(UUID playerUUID) {
        return getDataMap("player", playerUUID.toString());
    }

    public CoolTimeMap getPlayerCoolTimeMap(A_Player player) {
        return getPlayerCoolTimeMap(player.getUniqueId());
    }

    public CoolTimeMap getPlayerCoolTimeMap(A_OfflinePlayer player) {
        return getPlayerCoolTimeMap(player.getUniqueId());
    }

    public CoolTimeMap getPlayerCoolTimeMap(UUID playerUUID) {
        return getCoolTimeMap("player", playerUUID.toString());
    }

    public void copyFrom(A_PluginData other) {
        this.dataMaps.putAll(other.dataMaps);
        this.coolTimes.putAll(other.coolTimes);
    }

    private void saveData(String type, String key, YamlConfiguration data) {
        DataStorage storage = CommediaDellartePlugin.manager.getStorage();
        storage.save(this.plugin.getName().toLowerCase(), type, key, data);
    }

    private YamlConfiguration loadData(String type, String key) {
        DataStorage storage = CommediaDellartePlugin.manager.getStorage();
        return storage.load(this.plugin.getName().toLowerCase(), type, key);
    }

    public void savePlayerData(A_OfflinePlayer player) {
        DataMap map = this.getPlayerDataMap(player);
        CoolTimeMap cool = this.getPlayerCoolTimeMap(player);

        YamlConfiguration config = new YamlConfiguration();
        config.set("data", map);
        config.set("cool", cool);
        
        saveData("player", player.getUniqueId().toString(), config);
    }

    public void loadPlayerData(A_OfflinePlayer player) {
        String key = player.getUniqueId().toString();
        YamlConfiguration config = loadData("player", key);

        setDataMap("player", key, config.getObject("data", DataMap.class, new DataMap()));
        setCoolTimeMap("player", key, config.getObject("cool", CoolTimeMap.class, new CoolTimeMap()));
    }

    public void saveWorldData(A_World world) {
        DataMap map = this.getWorldDataMap(world);
        CoolTimeMap cool = this.getWorldCoolTimeMap(world);

        YamlConfiguration config = new YamlConfiguration();
        config.set("data", map);
        config.set("cool", cool);
        
        saveData("world", world.getName(), config);
    }

    public void loadWorldData(A_World world) {
        String key = world.getName();
        YamlConfiguration config = loadData("world", key);

        setDataMap("world", key, config.getObject("data", DataMap.class, new DataMap()));
        setCoolTimeMap("world", key, config.getObject("cool", CoolTimeMap.class, new CoolTimeMap()));
    }

    public void saveEntitiesData() {
        DataStorage storage = CommediaDellartePlugin.manager.getStorage();
        Set<String> keys = this.dataMaps.get("entity").keySet();
        keys.addAll(this.coolTimes.get("entity").keySet());

        if (storage.getType() == DataStorage.Type.FILE) {
            YamlConfiguration config = new YamlConfiguration();

            keys.forEach(key -> {
                Entity e = Bukkit.getEntity(UUID.fromString(key));
                if (e != null) {
                    A_Entity entity = CommediaDellarte.getAEntity(e);
                    DataMap map = getEntityDataMap(entity);
                    CoolTimeMap cool = getEntityCoolTimeMap(entity);

                    config.set(key + ".data", map);
                    config.set(key + ".cool", cool);
                }
            });

            storage.save(plugin.getName().toLowerCase(), "entity", "entities", config);
            return;
        }

        ;

        keys.forEach(key -> {
            Entity e = Bukkit.getEntity(UUID.fromString(key));
            if (e != null) {
                A_Entity entity = CommediaDellarte.getAEntity(e);
                DataMap map = getEntityDataMap(entity);
                CoolTimeMap cool = getEntityCoolTimeMap(entity);
                
                YamlConfiguration config = new YamlConfiguration();
                config.set("data", map);
                config.set("cool", cool);
            
                saveData("entity", entity.getUniqueId().toString(), config);
            }
        });
    }

    public void loadEntitiesData() {
        DataStorage storage = CommediaDellartePlugin.manager.getStorage();

        if (storage.getType() == DataStorage.Type.FILE) {
            YamlConfiguration config = storage.load(pluginLowerCase(), "entity", "entities");
            if (config == null) return;

            for (String key : config.getKeys(false)) {
                Entity e = Bukkit.getEntity(UUID.fromString(key));
                if (e != null) {
                    DataMap map = (DataMap) config.get(key + ".data");
                    CoolTimeMap cool = (CoolTimeMap) config.get(key + ".cool");

                    if (map != null) {
                        this.dataMaps.get("entity").put(key, map);
                    }
                    if (cool != null) {
                        this.coolTimes.get("entity").put(key, cool);
                    }
                }
            }
            return;
        }

        Map<String, YamlConfiguration> loaded = storage.loadAll(pluginLowerCase(), "entity");
        loaded.forEach((uuid, config) -> {
            Entity e = Bukkit.getEntity(UUID.fromString(uuid));
            if (e != null) {
                DataMap map = (DataMap) config.get("data");
                CoolTimeMap cool = (CoolTimeMap) config.get("cool");

                if (map != null) {
                    this.dataMaps.get("entity").put(uuid, map);
                }
                if (cool != null) {
                    this.coolTimes.get("entity").put(uuid, cool);
                }
            }
        });
    }


    public static A_PluginData newPluginData(Plugin plugin) {
        return new A_PluginData(plugin, new HashMap<>(), new HashMap<>());
    }

    @Override
    public boolean containsDataMap(String type, String key) {
        return dataMaps.containsKey(type) && dataMaps.get(type).containsKey(key);
    }

    @Override
    public boolean containsCoolTimeMap(String type, String key) {
        return coolTimes.containsKey(type) && coolTimes.get(type).containsKey(key);
    }

    @Override
    public boolean containsEntityDataMap(A_Entity entity) {
        return containsDataMap("entity", entity.getUniqueId().toString());
    }

    @Override
    public boolean containsEntityCoolTimeMap(A_Entity entity) {
        return containsCoolTimeMap("entity", entity.getUniqueId().toString());
    }

    @Override
    public boolean containsWorldDataMap(A_World world) {
        return containsDataMap("world", world.getName());
    }

    @Override
    public boolean containsWorldDataMap(String worldName) {
        return containsDataMap("world", worldName);
    }

    @Override
    public boolean containsWorldCoolTimeMap(A_World world) {
        return containsCoolTimeMap("world", world.getName());
    }

    @Override
    public boolean containsWorldCoolTimeMap(String worldName) {
        return containsCoolTimeMap("world", worldName);
    }

    @Override
    public boolean containsPlayerDataMap(A_Player player) {
        return containsDataMap("player", player.getUniqueId().toString());
    }

    @Override
    public boolean containsPlayerDataMap(A_OfflinePlayer player) {
        return containsDataMap("player", player.getUniqueId().toString());
    }

    @Override
    public boolean containsPlayerDataMap(UUID playerUUID) {
        return containsDataMap("player", playerUUID.toString());
    }

    @Override
    public boolean containsPlayerCoolTimeMap(A_Player player) {
        return containsCoolTimeMap("player", player.getUniqueId().toString());
    }

    @Override
    public boolean containsPlayerCoolTimeMap(A_OfflinePlayer player) {
        return containsCoolTimeMap("player", player.getUniqueId().toString());
    }

    @Override
    public boolean containsPlayerCoolTimeMap(UUID playerUUID) {
        return containsCoolTimeMap("player", playerUUID.toString());
    }

    @Override
    public void copyFrom(PluginData other) {
        if (other instanceof A_PluginData otherData) {
            this.dataMaps.clear();
            this.coolTimes.clear();
            this.dataMaps.putAll(otherData.dataMaps);
            this.coolTimes.putAll(otherData.coolTimes);
        } else throw new IllegalArgumentException("Incompatible PluginData implementation");
    }

    public String pluginLowerCase() {
        return plugin.getName().toLowerCase();
    }
}
