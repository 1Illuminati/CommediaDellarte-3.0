package org.red.library.util;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record A_Data(Map<String, DataMap> dataMaps,
                     Map<String, CoolTimeMap> coolTimes) implements ConfigurationSerializable {

    public DataMap getDataMap(Plugin plugin) {
        return dataMaps.computeIfAbsent(plugin.getName(), k -> new DataMap());
    }

    public CoolTimeMap getCoolTime(Plugin plugin) {
        return coolTimes.computeIfAbsent(plugin.getName(), k -> new CoolTimeMap());
    }

    public void copy(A_Data aData) {
        this.dataMaps.clear();
        if (aData.dataMaps() != null) this.dataMaps.putAll(aData.dataMaps());
        this.coolTimes.clear();
        if (aData.coolTimes() != null) this.coolTimes.putAll(aData.coolTimes());
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
