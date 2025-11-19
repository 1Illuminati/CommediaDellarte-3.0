package org.red.minecraft.dellarte.data.storage;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.red.minecraft.dellarte.util.A_File;

public class FileStorage implements DataStorage {

    @Override
    public Type getType() {
        return Type.FILE;
    }

    @Override
    public void save(String plugin, String type, String key, YamlConfiguration data) {
        try {
            data.save(getAFile(plugin, type, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public YamlConfiguration load(String plugin, String type, String key) {
        return YamlConfiguration.loadConfiguration(getAFile(plugin, type, key));
    }

    @Override
    public void delete(String plugin, String type, String key) {
        getAFile(plugin, type, key).delete();
    }

    @Override
    public boolean contain(String plugin, String type, String key) {
        return getAFile(plugin, type, key).isFile();
    }


    protected A_File getAFile(String plugin, String type, String key) {
        return new A_File(plugin + "/" + type + "/" + key + ".yml");
    }

    @Override
    public Map<String, YamlConfiguration> loadAll(String plugin, String type) {
        Map<String, YamlConfiguration> out = new HashMap<>();
        A_File file = new A_File(plugin + "/" + type);

        for (String name : file.list()) {
            if (name.endsWith(".yml")) {
                String key = name.replace(".yml", "");
                out.put(key,  load(plugin, type, key));
            }
        }

        return out;
    }
}
