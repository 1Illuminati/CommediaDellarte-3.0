package org.red.minecraft.dellarte.library.data;

import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.red.library.data.DataMapManager;
import org.red.library.data.adapter.IAdapter;
import org.red.minecraft.dellarte.library.data.serialize.BoundingBoxSerialize;
import org.red.minecraft.dellarte.library.entity.A_Entity;
import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.user.A_OfflinePlayer;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;
import org.red.minecraft.dellarte.library.util.PairKeyMap;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.world.A_World;

public class PluginData {
    private static final String DATA_KEY = "data";
    private static final String COOL_KEY = "cool";
    private static final String ENTITY_KEY = "entity";
    private static final String PLAYER_KEY = "player";
    private static final String WORLD_KEY = "world";
    private final DataMapManager manager = new A_DataMapManager();
    private final PairKeyMap<String, String, A_DataMap> dataMaps = new PairKeyMap<>();

    public PluginData(Plugin plugin, Map<String, IAdapter> adapters) {
        adapters.forEach((type, adapter) -> manager.registerAdapter(type, adapter));
    }

    public A_DataMap getDataMap(String type, String key) {
        if (dataMaps.containsKeys(type, key)) return dataMaps.get(type, key).getDataMap(DATA_KEY);
        else if (manager.containDataMap(type, key)) dataMaps.put(type, key, A_DataMap.convert(manager.loadDataMap(type, key)));
        else dataMaps.put(type, key, new A_DataMap());

        return getDataMap(type, key);
    }

    public CoolTimeMap getCoolTimeMap(String type, String key) {
        if (dataMaps.containsKeys(type, key)) return dataMaps.get(type, key).getClass(COOL_KEY, CoolTimeMap.class, new CoolTimeMap());
        else if (manager.containDataMap(type, key)) dataMaps.put(type, key, A_DataMap.convert(manager.loadDataMap(type, key)));
        else dataMaps.put(type, key, new A_DataMap());

        return getCoolTimeMap(type, key);
    }

    public boolean containsDataMap(String type, String key) {
        return !dataMaps.containsKeys(type, key) ? false : dataMaps.get(type, key).containsKey(DATA_KEY);
    }

    public boolean containsCoolTimeMap(String type, String key) {
        return !dataMaps.containsKeys(type, key) ? false : dataMaps.get(type, key).containsKey(COOL_KEY);
    }

    public A_DataMap getEntityDataMap(A_Entity entity) {
        if (entity instanceof A_Player player) return getPlayerDataMap((player));
        return getDataMap(ENTITY_KEY, entity.getUniqueId().toString());
    }

    public CoolTimeMap getEntityCoolTimeMap(A_Entity entity) {
        return getCoolTimeMap(ENTITY_KEY, entity.getUniqueId().toString());
    }

    public boolean containsEntityDataMap(A_Entity entity) {
        return containsDataMap(ENTITY_KEY, entity.getUniqueId().toString());
    }

    public boolean containsEntityCoolTimeMap(A_Entity entity) {
        return containsCoolTimeMap(ENTITY_KEY, entity.getUniqueId().toString());
    }

    public A_DataMap getWorldDataMap(A_World world) {
        return getWorldDataMap(world.getName());
    }

    public A_DataMap getWorldDataMap(String worldName) {
        return getDataMap(WORLD_KEY, worldName);
    }

    public boolean containsWorldDataMap(A_World world) {
        return containsWorldDataMap(world.getName());
    }

    public boolean containsWorldDataMap(String worldName) {
        return containsDataMap(WORLD_KEY, worldName);
    }

    public CoolTimeMap getWorldCoolTimeMap(A_World world) {
        return getWorldCoolTimeMap(world.getName());
    }

    public CoolTimeMap getWorldCoolTimeMap(String worldName) {
        return getCoolTimeMap(WORLD_KEY, worldName);
    }

    public boolean containsWorldCoolTimeMap(A_World world) {
        return containsWorldCoolTimeMap(world.getName());
    }

    public boolean containsWorldCoolTimeMap(String worldName) {
        return containsCoolTimeMap(WORLD_KEY, worldName);
    }

    public A_DataMap getPlayerDataMap(A_Player player) {
        return getPlayerDataMap(player.getUniqueId());
    }

    public A_DataMap getPlayerDataMap(A_OfflinePlayer player) {
        return getPlayerDataMap(player.getUniqueId());
    }

    public A_DataMap getPlayerDataMap(UUID playerUUID) {
        return getDataMap(PLAYER_KEY, playerUUID.toString());
    }

    public boolean containsPlayerDataMap(A_Player player) {
        return containsPlayerDataMap(player.getUniqueId());
    }

    public boolean containsPlayerDataMap(A_OfflinePlayer player) {
        return containsPlayerDataMap(player.getUniqueId());
    }

    public boolean containsPlayerDataMap(UUID playerUUID) {
        return containsCoolTimeMap(PLAYER_KEY, playerUUID.toString());
    }

    public CoolTimeMap getPlayerCoolTimeMap(A_Player player) {
        return getPlayerCoolTimeMap(player.getUniqueId());
    }

    public CoolTimeMap getPlayerCoolTimeMap(A_OfflinePlayer player) {
        return getPlayerCoolTimeMap(player.getUniqueId());
    }

    public CoolTimeMap getPlayerCoolTimeMap(UUID playerUUID) {
        return getCoolTimeMap(PLAYER_KEY, playerUUID.toString());
    }

    public boolean containsPlayerCoolTimeMap(A_Player player) {
        return containsPlayerCoolTimeMap(player.getUniqueId());
    }

    public boolean containsPlayerCoolTimeMap(A_OfflinePlayer player) {
        return containsPlayerCoolTimeMap(player.getUniqueId());
    }

    public boolean containsPlayerCoolTimeMap(UUID playerUUID) {
        return containsCoolTimeMap(DATA_KEY, playerUUID.toString());
    }

    public void copyFrom(PluginData other) {

    }

    private class A_DataMapManager extends DataMapManager {
        private A_DataMapManager() {
            super.registerSerializableClass(BoundingBox.class, new BoundingBoxSerialize());
        }
    }
}
