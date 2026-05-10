package org.red.minecraft.dellarte.library.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class A_Util {
    private A_Util() {
        throw new UnsupportedOperationException();
    }

    public static List<String> removeStringNotStartWith(List<String> list, String string) {
        if (list == null) return null;
        List<String> copyList = new ArrayList<>(list);
        list.stream().filter(s -> !s.startsWith(string)).forEach(copyList::remove);
        return copyList;
    }

    /**
     *
     * @param map
     * @return
     */
    public static Map<String, Object> deserializeAll(Map<String, Object> map) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof ConfigurationSerializable) {
                Map<String, Object> serialized = ((ConfigurationSerializable) value).serialize();
                result.put(key, deserializeAll(serialized));
            } else if (value instanceof Map) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> nested = (Map<String, Object>) value;
                    result.put(key, deserializeAll(nested));
                } catch (ClassCastException e) {
                    result.put(key, value);
                }
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    public static A_DataMap configSectionToADataMap(ConfigurationSection section) {
        A_DataMap map = new A_DataMap();

        for (String key : section.getKeys(false)) {
            Object value = section.get(key);

            if (value instanceof ConfigurationSection section1) map.put(key, configSectionToADataMap(section1));
            else map.put(key, value);
        }

        return map;
    }

}
