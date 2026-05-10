package org.red.minecraft.dellarte.library.util.config;

import org.bukkit.configuration.ConfigurationSection;
import org.red.minecraft.dellarte.library.exception.ConfigException;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.util.PairData;

public class Config {
    private final IConfigSchema schema;
    private final A_DataMap configData;

    public Config(IConfigSchema schema) {
        this(schema, new A_DataMap());
    }

    public Config(IConfigSchema schema, A_DataMap dataMap) {
        this.schema = schema;
        this.configData = dataMap;
        this.initAndValidate();
    }

    public Config(IConfigSchema schema, ConfigurationSection section) {
        this.schema = schema;
        this.configData = new A_DataMap();

        for (String key : section.getKeys(false)) this.configData.set(key, section.get(key));
        this.initAndValidate();
    }

    /**
     * 데이터를 최초값 할당 및 올바르게 데이터가 할당되었는지 검증되는 함수
     * 스키마에 필드에 해당하는 모든 데이터가 들어가 있어야하며 클래스(타입) 또한 올바르게 들어가진 상태여아한다
     * 만약 다를 경우 RunTimeException을 던짐
     * 상속받을 경우 수정 가능하게 제작
     */
    protected void initAndValidate() {
        //default 데이터 추가
        for (PairData<String, Object> data : schema.getDefault()) {
            if (!configData.containsKey(data.dataA())) configData.put(data.dataA(), data.dataB());
        }

        //데이터가 올바르게 존재하는지 확인
        for (PairData<String, Class<?>> field : schema.getField()) {
            String fieldName = field.dataA();
            if (!configData.containsKey(fieldName)) throw new ConfigException.ConfigDataNotFoundException(fieldName);
            if (!configData.get(fieldName).getClass().isAssignableFrom(field.dataB())) throw new ConfigException.ConfigIllegalDataException(fieldName, field.dataB());
        }
    }

    public IConfigSchema getSchema() {
        return this.schema;
    }

    public Object getData(String key) {
        return configData.get(key);
    }

    public String getDataString(String key) {
        return configData.getString(key);
    }

    public Integer getDataInteger(String key) {
        return configData.getInt(key);
    }

    public Double getDataDouble(String key) {
        return configData.getDouble(key);
    }

    public Boolean getDataBoolean(String key) {
        return configData.getBoolean(key);
    }
}
