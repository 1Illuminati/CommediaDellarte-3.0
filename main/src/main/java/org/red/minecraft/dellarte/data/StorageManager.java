package org.red.minecraft.dellarte.data;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.library.data.serialize.RegisterSerializable;
import org.red.library.data.serialize.RegisterSerializableHolder;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.data.adapter.*;
import org.red.minecraft.dellarte.library.data.AdapterFactory;
import org.red.minecraft.dellarte.library.exception.DataStorageNullException;
import org.red.minecraft.dellarte.library.data.IDataAdapter;
import org.red.minecraft.dellarte.library.data.IDataStorage;
import org.red.minecraft.dellarte.library.data.SaveConfig;
import org.red.minecraft.dellarte.library.exception.NonAdapterException;
import org.red.minecraft.dellarte.library.util.PairData;
import org.red.minecraft.dellarte.library.util.config.Config;
import org.red.minecraft.dellarte.library.util.config.IConfigSchema;
import org.red.minecraft.dellarte.library.util.map.NamespaceMap;

import java.util.HashMap;
import java.util.Map;


/**
 * 수정 매우 필요 매우 매우 매우 필요
 */
public final class StorageManager implements RegisterSerializableHolder {
    private final NamespaceMap<IDataStorage> map = new NamespaceMap<>();
    private final NamespaceMap<IDataStorage> tempMap = new NamespaceMap<>();
    private final NamespaceMap<BukkitTask> storageTaskMap = new NamespaceMap<>();

    private final Map<Class<?>, RegisterSerializable> registerSerializableMap = new HashMap<>();
    private final AdapterFactoryManager factoryManager = new AdapterFactoryManager();

    public AdapterFactory getFactory(NamespacedKey key) {
        return factoryManager.get(key);
    }

    public void setFactory(@Nullable AdapterFactory factory) {
        this.factoryManager.set(factory);
    }

    public boolean containsFactory(NamespacedKey key) {
        return this.factoryManager.contain(key);
    }

    public IDataStorage getStorage(NamespacedKey key) {
        if (!map.containsKey(key)) throw new DataStorageNullException(key);

        return map.get(key);
    }

    public IDataStorage getTempStorage(NamespacedKey key) {
        return tempMap.computeIfAbsent(key, k -> new DataStorage(null, new NoneAdapter()));
    }

    public void setStorageAutoSave(IDataStorage storage) {
        if (!storage.config().autoSaveEnable()) return;
        int delay = storage.config().autoSaveTime() * 20;
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
        ConfigurationSection storageSections = CommediaDellartePlugin.config.getConfigurationSection("data-storage");

        storageSections.getKeys(false).forEach(pluginName -> {
            String lowerPluginName = pluginName.toLowerCase();
            ConfigurationSection pluginSection = storageSections.getConfigurationSection(pluginName);
            pluginSection.getKeys(false).forEach(type -> {
                NamespacedKey key = new NamespacedKey(lowerPluginName, type);
                ConfigurationSection storageConfig = pluginSection.getConfigurationSection(type);

                this.createDataStorage(this.createSaveConfig(key, storageConfig));
            });


            createDefaultStorage(lowerPluginName, "player");
            createDefaultStorage(lowerPluginName, "entity");
            createDefaultStorage(lowerPluginName, "world");
        });
    }

    /**
     *
     * @param config
     * @return
     */
    public void createDataStorage(SaveConfig config) {
        NamespacedKey adapterKey = config.saveType();
        if (!this.factoryManager.contain(adapterKey))
            throw new NonAdapterException(adapterKey);

        IDataAdapter adapter = this.factoryManager.createAdapter(adapterKey, config.adapterConfig());
        IDataStorage storage = new DataStorage(config, adapter);
        this.map.put(config.getKey(), storage);
        this.setStorageAutoSave(storage);
        CommediaDellartePlugin.sendDebugLog(String.format("Created DataStorage for %s", config.getKey()));
    }

    /**
     * 설정하지 않을 경우 제공하는 디폴드 데이터 스토리지 제작
     * Player, Entity, World의 자동설정 기능으로만 사용
     * @param pluginName 키
     * @param type 타입
     */
    private void createDefaultStorage(String pluginName, String type) {
        NamespacedKey key = new NamespacedKey(pluginName, type);

        if (!map.containsKey(key)) {
            this.createDataStorage(this.createSaveConfig(key, null));
        }
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

    /**
     * SaveConfig를 제작하는 함수
     * storageConfig을 null로 넣을 경우 기본 saveConfig값을 완성하여 반환한다
     * @param nameSpace 데이터 스토리지의 키(플러그인 + 타입)
     * @param storageConfig savetype, autoSave설정
     * @return 완성된 saveConfig값
     */
    public SaveConfig createSaveConfig(NamespacedKey nameSpace, @Nullable ConfigurationSection storageConfig) {
        if (storageConfig == null) {
            Config config = this.createAdapterConfig(nameSpace, null, this.factoryManager.get(FileAdapterFactory.KEY).getConfigSchema());
            return new SaveConfig(nameSpace, true, FileAdapterFactory.KEY, 300, config);
        }

        boolean enable = storageConfig.getBoolean("enable", true);
        String saveTypeStr = storageConfig.contains("saveType") ? storageConfig.getString("saveType") : "file";
        int autoSaveTime = storageConfig.getInt("autoSaveTime", 300);

        //기본 dellarte 지원 파일 저장 형식들은 굳이 플러그인명을 치지 않아도 된다
        NamespacedKey saveType = switch (saveTypeStr) {
            case "file" -> FileAdapterFactory.KEY;
            case "mysql" -> MySqlAdapterFactory.KEY;
            case "none" -> NoneAdapterFactory.KEY;
            default -> {
                String[] split = saveTypeStr.split(":");
                String pluginName = split[0];
                String saveTypeName = split[1];
                yield new NamespacedKey(pluginName.toLowerCase(), saveTypeName);
            }
        };

        IConfigSchema schema = this.factoryManager.getFactorySchema(saveType);
        ConfigurationSection adapterSection = storageConfig.getConfigurationSection("config");
        Config adapterConfig = createAdapterConfig(nameSpace, adapterSection, schema);

        return new SaveConfig(nameSpace, enable, saveType, autoSaveTime, adapterConfig);
    }

    private @NotNull Config createAdapterConfig(NamespacedKey nameSpace, ConfigurationSection adapterSection, IConfigSchema schema) {
        Config adapterConfig = adapterSection == null ? new Config(schema) : new Config(schema, adapterSection);

        //%type% 같은 특수한 데이터들을 사전 처리
        for (PairData<String, Class<?>> key : schema.getField()) {
            String fieldName = key.dataA();

            Object data = adapterConfig.getData(fieldName);

            if (data instanceof String strData) {
                strData = strData
                        .replace("%type%", nameSpace.getKey())
                        .replace("%plugin_name%", nameSpace.getNamespace());

                adapterConfig.setData(fieldName, strData);
            }
        }
        return adapterConfig;
    }

    @Override
    public <T> void registerSerializableClass(Class<T> clazz, RegisterSerializable<T> registerSerializable) {
        this.registerSerializableMap.put(clazz, registerSerializable);
    }

    @Override
    public <T> RegisterSerializable getSerializableClass(Class<T> clazz) {
        return this.registerSerializableMap.getOrDefault(clazz, null);
    }

    @Override
    public boolean containSerializableClass(Class<?> clazz) {
        return this.registerSerializableMap.containsKey(clazz);
    }
}
