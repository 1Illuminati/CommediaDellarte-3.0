package org.red.library.util;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class A_Data implements ConfigurationSerializable {
    private final Map<String, DataMap> dataMaps;
    private final Map<String, CoolTimeMap> coolTimes;
    public A_Data(Map<String, DataMap> dataMaps, Map<String, CoolTimeMap> coolTimes) {
        this.dataMaps = dataMaps;
        this.coolTimes = coolTimes;
    }

    public DataMap getDataMap(Plugin plugin) {
        return dataMaps.computeIfAbsent(plugin.getName(), k -> new DataMap());
    }

    public Map<String, DataMap> getDataMaps() {
        return dataMaps;
    }

    public CoolTimeMap getCoolTime(Plugin plugin) {
        return coolTimes.computeIfAbsent(plugin.getName(), k -> new CoolTimeMap());
    }

    public Map<String, CoolTimeMap> getCoolTimes() {
        return coolTimes;
    }

    public void copy(A_Data aData) {
        this.dataMaps.clear();
        if (aData.getDataMaps() != null) this.dataMaps.putAll(aData.getDataMaps());
        this.coolTimes.clear();
        if (aData.getCoolTimes() != null) this.coolTimes.putAll(aData.getCoolTimes());
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("dataMaps", dataMaps);
        map.put("coolTimes", coolTimes);
        return map;
    }

    @NotNull
    public static A_Data deserialize(Map<String, Object> map) {
        return new A_Data((Map<String, DataMap>) map.get("dataMaps"), (Map<String, CoolTimeMap>) map.get("coolTimes"));
    }

    public static A_Data newAData() {
        return new A_Data(new HashMap<>(), new HashMap<>());
    }
}
