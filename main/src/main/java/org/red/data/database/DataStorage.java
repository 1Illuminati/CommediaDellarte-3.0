package org.red.data.database;

import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

public interface DataStorage {
    // 기본 저장/로드
    void save(String type, String key, YamlConfiguration data);

    YamlConfiguration load(String type, String key);

    // 삭제
    void delete(String type, String key);

    // 플레이어 / 엔티티 / 월드 전용
    void savePlayer(UUID player, YamlConfiguration data);

    YamlConfiguration loadPlayer(UUID player);

    void saveEntity(UUID entityId, YamlConfiguration data);

    YamlConfiguration loadEntity(UUID entityId);

    void saveWorld(String worldName, YamlConfiguration data);

    YamlConfiguration loadWorld(String worldName);
    
    Type getType();

    public enum Type {
        MYSQL,
        SQLITE,
        MARIADB,
        POSTGRESQL,
        SQLSERVER,
        MONGODB,
        File
    }
}

