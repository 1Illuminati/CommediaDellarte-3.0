package org.red.data.database;

import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.red.util.A_File;

public class FileStorage implements DataStorage {

    @Override
    public Type getType() {
        return Type.File;
    }

    @Override
    public void save(String type, String key, YamlConfiguration data) {
        try {
            data.save(new A_File(type + "/" + key + ".yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public YamlConfiguration load(String type, String key) {
        return YamlConfiguration.loadConfiguration(new A_File(type + "/" + key + ".yml"));
    }

    @Override
    public void delete(String type, String key) {
        new A_File(type + "/" + key + ".yml").delete();
    }

    @Override
    public void savePlayer(UUID player, YamlConfiguration data) {
        save("player", player.toString(), data);
    }

    @Override
    public YamlConfiguration loadPlayer(UUID player) {
        return load("player", player.toString());
    }

    @Override
    public void saveEntity(UUID entityId, YamlConfiguration data) {
        save("entity", entityId.toString(), data);
    }

    @Override
    public YamlConfiguration loadEntity(UUID entityId) {
        return load("entity", entityId.toString());
    }

    @Override
    public void saveWorld(String worldName, YamlConfiguration data) {
        save("world", worldName, data);
    }

    @Override
    public YamlConfiguration loadWorld(String worldName) {
        return load("world", worldName);
    }

}
