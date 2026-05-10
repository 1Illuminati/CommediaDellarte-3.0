package org.red.minecraft.dellarte.data;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.library.data.IDataAdapter;
import org.red.minecraft.dellarte.library.data.IDataStorage;import org.red.minecraft.dellarte.library.data.SaveConfig;
import org.red.minecraft.dellarte.library.util.A_DataMap;

import java.util.HashMap;

public final class DataStorage implements IDataStorage {
    private final SaveConfig config;
    private final IDataAdapter adapter;
    private final HashMap<String, A_DataMap> dataMap = new HashMap<>();

    public DataStorage(SaveConfig config, IDataAdapter adapter) {
        this.config = config;
        this.adapter = adapter;
    }

    @Override
    public SaveConfig config() {
        return this.config;
    }

    /**
     * 데이터 맵 불러오기
     * 데이터가 존재하지 않을경우 null을 반환한다
     */
    @Nullable
    @Override
    public A_DataMap getDataMap(String key) {
        if (loadedData(key)) {
            return dataMap.get(key);
        }
        else if (containData(key)) {
            loadData(key);
        }
        else dataMap.put(key, new A_DataMap());

        return dataMap.get(key);
    }

    /**
     * 현재 이 데이터가 로드가 완료되어 데이터가 저장상태로 존재하는게 아닌 메모리상에 존재하는지 확인할때 사용
     */
    @Override
    public boolean loadedData(String key) {
        return this.dataMap.containsKey(key);
    }

    /**
     * 저장된 데이터가 존재하는지 확인할때 사용
     */
    @Override
    public boolean containData(String key) {
        return this.adapter.containDataMap(key);
    }

    /**
     * 데이터 저장
     */
    @Override
    public void saveData(String key) {
        this.adapter.saveDataMap(key, this.getDataMap(key));
    }

    /**
     * 데이터 로드
     */
    @Override
    public void loadData(String key) {
        dataMap.put(key, this.adapter.loadDataMap(key));
    }

    /**
     * 데이터 삭제
     */
    @Override
    public void deleteData(String key) {
        this.adapter.deleteDataMap(key);
    }

    /**
     * 모든 데이터 저장
     */
    @Override
    public void saveAll() {
        this.dataMap.keySet().forEach(this::saveData);
    }

    /**
     * 모든 데이터 로드
     */
    @Override
    public void loadAll() {
        this.dataMap.keySet().forEach(this::loadData);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.config.getKey();
    }
}
