package org.red.minecraft.dellarte.data;

import java.util.HashMap;

import org.bukkit.NamespacedKey;
import org.red.library.data.DataMapManager;
import org.red.library.data.adapter.FileAdapter;
import org.red.library.data.adapter.IAdapter;
import org.red.library.data.serialize.RegisterSerializable;
import org.red.minecraft.dellarte.library.data.IDataStroage;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;
import org.red.minecraft.dellarte.util.A_File;

public class DataStorage extends DataMapManager implements IDataStroage {
    public static DataStorage createDefaultDataStorage(NamespacedKey key) {
        return new DataStorage(SaveConfig.createDefaultConfig(key), new FileAdapter(new A_File(key.getNamespace() + "/" + key.getKey())));
    }

    private static final HashMap<Class<?>, RegisterSerializable<?>> REG_SERIALIZE_MAP = new HashMap<>();
    private final HashMap<String, A_DataMap> dataMaps = new HashMap<>();
    private final SaveConfig config;

    public DataStorage(SaveConfig config, IAdapter adapter) {
        super(adapter);
        this.config = config;
    }

    public static <T> void regSerializableClass(Class<T> clazz, RegisterSerializable<T> registerSerializable) {
        DataStorage.REG_SERIALIZE_MAP.put(clazz, registerSerializable);
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
    public A_DataMap getDataMap(String key) {
        return getData(key).getDataMap("data");
    }

    @Override
    public CoolTimeMap getCoolTimeMap(String key) {
        return getData(key).getClass("cool", CoolTimeMap.class);
    }

    public A_DataMap getData(String key) {
        if (loadedData(key)) return dataMaps.get(key);
        else if (containData(key)) loadData(key);
        else dataMaps.put(key, new A_DataMap());

        return getData(key);
    }

    @Override
    public boolean loadedData(String key) {
        return dataMaps.containsKey(key);
    }

    @Override
    public boolean containData(String key) {
        return dataMaps.containsKey(key);
    }

    @Override
    public void saveData(String key) {
        super.saveDataMap(key, getDataMap(key));
    }

    @Override
    public void loadData(String key) {
        dataMaps.put(key, A_DataMap.convert(super.loadDataMap(key)));
    }

    @Override
    public void deleteData(String key) {
        super.deleteDataMap(key);
    }

    @Override
    public void loadAll() {
        super.getAdapter().loadAllKey().forEach(key -> super.loadDataMap(key));
    }

    @Override
    public void saveAll() {
        this.dataMaps.keySet().forEach(key -> saveData(key));
    }
}
