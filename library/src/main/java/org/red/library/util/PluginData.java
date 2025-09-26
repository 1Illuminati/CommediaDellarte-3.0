package org.red.library.util;

import java.util.UUID;

import org.red.library.entity.A_Entity;
import org.red.library.entity.A_Player;
import org.red.library.user.A_OfflinePlayer;
import org.red.library.world.A_World;

public interface PluginData {
    public DataMap getDataMap(String type, String key);

    public CoolTimeMap getCoolTimeMap(String type, String key);

    public boolean containsDataMap(String type, String key);

    public boolean containsCoolTimeMap(String type, String key);

    public DataMap getEntityDataMap(A_Entity entity);

    public CoolTimeMap getEntityCoolTimeMap(A_Entity entity);

    public boolean containsEntityDataMap(A_Entity entity);

    public boolean containsEntityCoolTimeMap(A_Entity entity);

    public DataMap getWorldDataMap(A_World world);

    public DataMap getWorldDataMap(String worldName);

    public boolean containsWorldDataMap(A_World world);

    public boolean containsWorldDataMap(String worldName);

    public CoolTimeMap getWorldCoolTimeMap(A_World world);

    public CoolTimeMap getWorldCoolTimeMap(String worldName);

    public boolean containsWorldCoolTimeMap(A_World world);

    public boolean containsWorldCoolTimeMap(String worldName);

    public DataMap getPlayerDataMap(A_Player player);

    public DataMap getPlayerDataMap(A_OfflinePlayer player);

    public DataMap getPlayerDataMap(UUID playerUUID);

    public boolean containsPlayerDataMap(A_Player player);

    public boolean containsPlayerDataMap(A_OfflinePlayer player);

    public boolean containsPlayerDataMap(UUID playerUUID);

    public CoolTimeMap getPlayerCoolTimeMap(A_Player player);

    public CoolTimeMap getPlayerCoolTimeMap(A_OfflinePlayer player);

    public CoolTimeMap getPlayerCoolTimeMap(UUID playerUUID);

    public boolean containsPlayerCoolTimeMap(A_Player player);

    public boolean containsPlayerCoolTimeMap(A_OfflinePlayer player);

    public boolean containsPlayerCoolTimeMap(UUID playerUUID);

    public void copyFrom(PluginData other);
}
