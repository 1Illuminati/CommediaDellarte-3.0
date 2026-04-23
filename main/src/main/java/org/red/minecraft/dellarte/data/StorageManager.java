package org.red.minecraft.dellarte.data;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;
import org.red.library.data.adapter.DatabaseAdapter;
import org.red.library.data.adapter.FileAdapter;
import org.red.library.data.adapter.IAdapter;
import org.red.library.data.adapter.MySqlAdapter;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.Config;
import org.red.minecraft.dellarte.data.adapter.NoneAdapter;
import org.red.minecraft.dellarte.exception.DataStorageNullException;
import org.red.minecraft.dellarte.library.data.IDataStorage;
import org.red.minecraft.dellarte.library.util.NamespaceMap;
import org.red.minecraft.dellarte.util.A_File;

import java.sql.SQLException;

public final class StorageManager {
    private final NamespaceMap<IDataStorage> map = new NamespaceMap<>();
    private final NamespaceMap<BukkitTask> storageTaskMap = new NamespaceMap<>();

    public IDataStorage getStorage(NamespacedKey key) {
        if (!map.containsKey(key)) throw new DataStorageNullException(key);

        return map.get(key);
    }

    public void setStorageAutoSave(IDataStorage storage) {
        int delay = ((DataStorage) storage).config().getAutoSaveTime() * 20;
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(CommediaDellartePlugin.instance, () -> {
                    storage.saveAll();
                },
                delay, delay);

        storageTaskMap.put(storage.getKey(), task);
    }

    /**
     * 데이터 스토리지을 콘피그에서 불러와 생성하는 함수
     */
    public void createDataStorages() {
        ConfigurationSection section = CommediaDellartePlugin.config.getConfigurationSection("data-storage");

        section.getKeys(false).forEach(key -> {
            ConfigurationSection pluginSection = section.getConfigurationSection(key);
            pluginSection.getKeys(false).forEach(type -> {
                SaveConfig config = SaveConfig.createSaveConfig(new NamespacedKey(key.toLowerCase(), type), pluginSection.getConfigurationSection(key));
                DataStorage storage = new DataStorage(config, createAdapter(config));
                map.put(config.getKey(), storage);
                setStorageAutoSave(storage);
                CommediaDellartePlugin.sendDebugLog("CreateStroage - " + storage.getKey());
            });


            if (!pluginSection.contains("player"))
                createDefaultStorage(key, "player");
            if (!pluginSection.contains("entity"))
                createDefaultStorage(key, "entity");
            if (!pluginSection.contains("world"))
                createDefaultStorage(key, "world");
        });
    }

    public void createDefaultStorage(String key, String type) {
        NamespacedKey nkey = new NamespacedKey(key.toLowerCase(), type);
        map.put(nkey, DataStorage.createDefaultDataStorage(nkey));
    }

    /**
     * 데이터 스토리지 생성에 필요한 어뎁터를 생성하는 함수
     */
    public IAdapter createAdapter(SaveConfig config) {
        IAdapter result = null;

        if (!config.isEnable()) return new NoneAdapter();

        switch(config.getSaveType()) {
            case FILE: {
                result = new FileAdapter(new A_File(config.getKey().getNamespace() + "/" + config.getKey().getKey()));
                break;
            }
            case MYSQL: {
                try {
                    result = new MySqlAdapter(getDBConfigLoad(config.getKey()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
        };

        return result;
    }

    /**
     * 데이터베이스 어댑터에 필요한 설정을 생성하는 함수
     * 에러가 날 경우 처리는 안되어있다
     */
    public DatabaseAdapter.Config getDBConfigLoad(NamespacedKey key) {
        if (!Config.DATABASE_ENABLED.asBooleanValue()) {
            return null;
        }

        String host = Config.DATABASE_HOST.asStringValue();
        String database = Config.DATABASE_NAME.asStringValue();
        int port = Config.DATABASE_PORT.asIntValue();
        String user = Config.DATABASE_USER.asStringValue();
        String password = Config.DATABASE_PASSWORD.asStringValue();

        return new DatabaseAdapter.Config(host, database, port, user, password, key.toString().replace(":", "_"));
    }

    public boolean containStorage(NamespacedKey key) {
        return map.containsKey(key);
    }

    public void allDataSave() {
        map.values().forEach(IDataStorage::saveAll);
    }

    public void allDataLoad() {
        map.values().forEach(IDataStorage::loadAll);
    }
}
