package org.red.library.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PairKeyMap<K1, K2, V> {
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
}
