package org.red.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.red.CommediaDellartePlugin;
import org.red.Config;
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

    /**
     * 
     * @param type
     * @param key
     * @return
     */
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

    public void saveData() {
        YamlConfiguration yamlConfig = new YamlConfiguration();
        yamlConfig.set("data_maps", dataMaps);
        yamlConfig.set("cool_times", coolTimes);
        
        if (Config.DATABASE_ENABLED.asBooleanValue()) saveDataDB(yamlConfig);
        else {
            try {
                yamlConfig.save(new A_File("plugin_data/" + plugin.getName() + ".yml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CommediaDellartePlugin.sendDebugLog("Saving Plugin Data for " + plugin.getName());
    }

    public void loadData() {
        
    }

    public void savePlayerData(A_Player player) {

    }
    
    /**
     * DB에 데이터 저장하는 코드
     * 각 type값을 받아와 지원하는 db의 경우 jdbc url을 만들어 접속 후 데이터를 저장
     */
    private void saveDataDB(YamlConfiguration yamlConfig) {
        int port = Config.DATABASE_PORT.asIntValue();
        String type = Config.DATABASE_TYPE.asStringValue();
        String host = Config.DATABASE_HOST.asStringValue();
        String name = Config.DATABASE_NAME.asStringValue();
        String user = Config.DATABASE_USER.asStringValue();
        String password = Config.DATABASE_PASSWORD.asStringValue();

        String url = makeJdbcUrl(type, host, port, name);

        // JDBC 4.0 이후 → Class.forName(...) 필요 없음
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO map_storage (data_string) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, yamlConfig.saveToString());
                pstmt.executeUpdate();
                System.out.println("데이터 저장 완료!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 지원하는 DB의 jdbc url 생성
     */
    private static String makeJdbcUrl(String dbType, String host, int port, String dbName) {
        switch (dbType.toLowerCase()) {
            case "mysql":
                return "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
            case "postgresql":
                return "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            case "sqlserver":
                return "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbName;
            default:
                throw new IllegalArgumentException("지원하지 않는 DB 타입: " + dbType);
        }
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
}
