package org.red.data.storage;

import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

public interface DataStorage {
    void save(String plugin, String type, String key, YamlConfiguration data);

    YamlConfiguration load(String plugin, String type, String key);

    void delete(String plugin, String type, String key);

    boolean contain(String plugin, String type, String key);

    Type getType();

    Map<String, YamlConfiguration> loadAll(String plugin, String type);

    public enum Type {
        MYSQL,
        SQLITE,
        MARIADB,
        POSTGRESQL,
        SQLSERVER,
        MONGODB,
        FILE
    }
}

