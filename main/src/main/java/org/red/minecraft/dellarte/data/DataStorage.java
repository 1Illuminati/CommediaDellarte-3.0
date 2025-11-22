package org.red.minecraft.dellarte.data;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.red.library.data.DataMapManager;
import org.red.library.data.adapter.IAdapter;
import org.red.library.data.serialize.RegisterSerializable;
import org.red.minecraft.dellarte.library.data.IDataStroage;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;

public class DataStorage extends DataMapManager implements IDataStroage {
    private static HashMap<Class<?>, RegisterSerializable<?>> REG_SERIALIZE_MAP = new HashMap<>();
    private final HashMap<UUID, A_DataMap> dataMaps = new HashMap<>();
    private final SaveConfig config;

    public DataStorage(SaveConfig config, IAdapter adapter) {
        super(adapter);
        this.config = config;
    }

    @Override
    public <T> void registerSerializableClass(Class<T> clazz, RegisterSerializable<T> registerSerializable) {
        DataStorage.REG_SERIALIZE_MAP.put(clazz, registerSerializable);
    }

    @Override
    public <T> RegisterSerializable<T> getSerializableClass(Class<T> clazz) {
        return (RegisterSerializable<T>) DataStorage.REG_SERIALIZE_MAP.getOrDefault(clazz, null);
    }

    @Override
    public boolean containSerializableClass(Class<?> clazz) {
        return DataStorage.REG_SERIALIZE_MAP.containsKey(clazz);
    }

    public SaveConfig config() {
        return this.config;
    }
    

    @Override
    public NamespacedKey getKey() {
        return config.getKey();
    }

    @Override
    public A_DataMap getDataMap(UUID key) {
        return dataMaps.get(key).getDataMap("data");
    }

    @Override
    public CoolTimeMap getCoolTimeMap(UUID key) {
        return dataMaps.get(key).getClass("cool", CoolTimeMap.class);
    }

    @Override
    public boolean loadedData(UUID key) {
        return dataMaps.containsKey(key);
    }

    @Override
    public boolean containData(UUID key) {
        return dataMaps.containsKey(key);
    }

    @Override
    public void saveData(UUID key) {
        super.saveDataMap(key.toString(), getDataMap(key));
    }

    @Override
    public void loadData(UUID key) {
        super.loadDataMap(key.toString());
    }

    @Override
    public void deleteData(UUID key) {
        super.deleteDataMap(key.toString());
    }

    @Override
    public void saveAll() {
        this.dataMaps.keySet().forEach(key -> saveData(key));
    }
}
