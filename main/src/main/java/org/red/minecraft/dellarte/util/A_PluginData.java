package org.red.minecraft.dellarte.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.red.minecraft.dellarte.data.storage.DataStorage;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.data.PluginData;
import org.red.minecraft.dellarte.library.entity.A_Entity;
import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.user.A_OfflinePlayer;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.world.A_World;
import org.red.minecraft.dellarte.CommediaDellartePlugin;

public class A_PluginData implements PluginData {
    private final Plugin plugin;
    private final Map<String, A_DataMap> dataMaps;
    private final Map<String, A_DataMap> coolTimes;

    private A_PluginData(Plugin plugin, Map<String, A_DataMap> dataMaps, Map<String, A_DataMap> coolTimes) {
        this.plugin = plugin;
        this.dataMaps = dataMaps;
        this.coolTimes = coolTimes;
    }

    private void setDataMap(String type, String key, A_DataMap map) {
        dataMaps.computeIfAbsent(type, k -> new A_DataMap()).put(key, map);
    }

    private void setCoolTimeMap(String type, String key, CoolTimeMap map) {
        coolTimes.computeIfAbsent(type, k -> new A_DataMap()).put(key, map);
    }

    public A_DataMap getDataMap(String type, String key) {
        return dataMaps.computeIfAbsent(type, k -> new A_DataMap()).getDataMap(key);
    }

    public CoolTimeMap getCoolTimeMap(String type, String key) {
        return coolTimes.computeIfAbsent(type, k -> new A_DataMap()).getClass(key, CoolTimeMap.class, new CoolTimeMap());
    }

    public A_DataMap getEntityDataMap(A_Entity entity) {
        if (entity instanceof A_Player player)
            return getPlayerDataMap(player);

        return getDataMap("entity", entity.getUniqueId().toString());
    }

    public CoolTimeMap getEntityCoolTimeMap(A_Entity entity) {
        if (entity instanceof A_Player player)
            return getPlayerCoolTimeMap(player);

        return getCoolTimeMap("entity", entity.getUniqueId().toString());
    }

    public A_DataMap getWorldDataMap(A_World world) {
        return getWorldDataMap(world.getName());
    }

    public A_DataMap getWorldDataMap(String worldName) {
        return getDataMap("world", worldName);
    }

    public CoolTimeMap getWorldCoolTimeMap(A_World world) {
        return getWorldCoolTimeMap(world.getName());
    }

    public CoolTimeMap getWorldCoolTimeMap(String worldName) {
        return getCoolTimeMap("world", worldName);
    }

    public A_DataMap getPlayerDataMap(A_Player player) {
        return getPlayerDataMap(player.getUniqueId());
    }

    public A_DataMap getPlayerDataMap(A_OfflinePlayer player) {
        return getPlayerDataMap(player.getUniqueId());
    }

    public A_DataMap getPlayerDataMap(UUID playerUUID) {
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
        A_DataMap map = this.getPlayerDataMap(player);
        CoolTimeMap cool = this.getPlayerCoolTimeMap(player);

        YamlConfiguration config = new YamlConfiguration();
        config.set("data", map);
        config.set("cool", cool);
        
        saveData("player", player.getUniqueId().toString(), config);
    }

    public void loadPlayerData(A_OfflinePlayer player) {
        String key = player.getUniqueId().toString();
        YamlConfiguration config = loadData("player", key);

        setDataMap("player", key, config.getObject("data", A_DataMap.class, new A_DataMap()));
        setCoolTimeMap("player", key, config.getObject("cool", CoolTimeMap.class, new CoolTimeMap()));
    }

    public void saveWorldData(A_World world) {
        A_DataMap map = this.getWorldDataMap(world);
        CoolTimeMap cool = this.getWorldCoolTimeMap(world);

        YamlConfiguration config = new YamlConfiguration();
        config.set("data", map);
        config.set("cool", cool);
        
        saveData("world", world.getName(), config);
    }

    public void loadWorldData(A_World world) {
        String key = world.getName();
        YamlConfiguration config = loadData("world", key);

        setDataMap("world", key, config.getObject("data", A_DataMap.class, new A_DataMap()));
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
                    A_DataMap map = getEntityDataMap(entity);
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
                A_DataMap map = getEntityDataMap(entity);
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
                    A_DataMap map = (A_DataMap) config.get(key + ".data");
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
                A_DataMap map = (A_DataMap) config.get("data");
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
