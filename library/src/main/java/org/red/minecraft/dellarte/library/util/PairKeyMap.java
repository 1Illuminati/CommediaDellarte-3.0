package org.red.minecraft.dellarte.library.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class PairKeyMap<K1, K2, V> implements ConfigurationSerializable {
    private record PairKey<K1, K2>(K1 key1, K2 key2) {

        @Override
            public String toString() {
                return "PairKey{" +
                        "key1=" + key1 +
                        ", key2=" + key2 +
                        '}';
            }
        }

    private final HashMap<PairKey<K1, K2>, V> map = new HashMap<>();

    public void put(K1 key1, K2 key2, V value) {
        PairKey<K1, K2> key = new PairKey<>(key1, key2);
        map.put(key, value);
    }

    public V get(K1 key1, K2 key2) {
        PairKey<K1, K2> key = new PairKey<>(key1, key2);
        return map.get(key);
    }

    public boolean containsKeys(K1 key1, K2 key2) {
        PairKey<K1, K2> key = new PairKey<>(key1, key2);
        return map.containsKey(key);
    }

    public V remove(K1 key1, K2 key2) {
        PairKey<K1, K2> key = new PairKey<>(key1, key2);
        return map.remove(key);
    }

    public Map<K1, V> getByFirstKey(K1 key1) {
        Map<K1, V> resultMap = new HashMap<>();
        for (Map.Entry<PairKey<K1, K2>, V> entry : map.entrySet()) {
            PairKey<K1, K2> key = entry.getKey();
            if (Objects.equals(key.key1, key1)) {
                resultMap.put(key.key1, entry.getValue());
            }
        }
        return resultMap;
    }

    public Map<K2, V> getBySecondKey(K2 key2) {
        Map<K2, V> resultMap = new HashMap<>();
        for (Map.Entry<PairKey<K1, K2>, V> entry : map.entrySet()) {
            PairKey<K1, K2> key = entry.getKey();
            if (Objects.equals(key.key2, key2)) {
                resultMap.put(key.key2, entry.getValue());
            }
        }
        return resultMap;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serializedMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        for (Map.Entry<PairKey<K1, K2>, V> entry : map.entrySet()) {
            PairKey<K1, K2> key = entry.getKey();
            String keyString = key.key1.toString() + "|" + key.key2.toString();
            V value = entry.getValue();

            if (value instanceof ConfigurationSerializable serializable) {
                data.put(keyString, serializable.serialize());
            } else {
                data.put(keyString, value); // fallback
            }
        }

        serializedMap.put("data", data);
        return serializedMap;
    }

    public static <K1, K2, V> PairKeyMap<K1, K2, V> deserialize(Map<String, Object> serialized) {
        PairKeyMap<K1, K2, V> pairMap = new PairKeyMap<>();

        Object raw = serialized.get("data");
        if (!(raw instanceof Map<?, ?> rawMap)) return pairMap;

        for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
            String keyString = (String) entry.getKey();
            String[] keys = keyString.split("\\|", 2);
            if (keys.length != 2) continue;

            // Casting with warning: the caller must know the types
            K1 key1 = (K1) keys[0];
            K2 key2 = (K2) keys[1];

            Object valueObj = entry.getValue();

            V value;
            if (valueObj instanceof Map<?, ?> valueMap) {
                // Try to reconstruct if value implements ConfigurationSerializable
                value = (V) ConfigurationSerialization.deserializeObject((Map<String, Object>) valueMap);
            } else {
                value = (V) valueObj;
            }

            pairMap.put(key1, key2, value);
        }

        return pairMap;
    }
}
