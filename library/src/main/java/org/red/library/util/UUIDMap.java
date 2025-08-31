package org.red.library.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class UUIDMap<T> implements ConfigurationSerializable {
    private final Map<UUID, T> map = new HashMap<>();

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public String toString()   {
        return map.toString();
    }

    public T get(Object key) {
        return map.get(key);
    }

    public T put(UUID key, T value) {
        return map.put(key, value);
    }

    public T remove(Object key) {
        return map.remove(key);
    }

    public void putAll(@NotNull Map<UUID, ? extends T> m) {
        map.putAll(m);
    }

    public void putAll(@NotNull UUIDMap<T> m) {
        map.putAll(m.map);
    }

    public void clear() {
        map.clear();
    }

    public Set<UUID> keySet() {
        return map.keySet();
    }

    public Collection<T> values() {
        return map.values();
    }

    public Set<Map.Entry<UUID, T>> entrySet() {
        return map.entrySet();
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    public T getOrDefault(Object key, T defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super UUID, ? super T> action) {
        map.forEach(action);
    }

    public void replaceAll(BiFunction<? super UUID, ? super T, ? extends T> function) {
        map.replaceAll(function);
    }

    public T putIfAbsent(UUID key, T value) {
        return map.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    public boolean replace(UUID key, T oldValue, T newValue) {
        return map.replace(key, oldValue, newValue);
    }

    public T replace(UUID key, T value) {
        return map.replace(key, value);
    }

    public T computeIfAbsent(UUID key, @NotNull Function<? super UUID, ? extends T> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    public T computeIfPresent(UUID key, @NotNull BiFunction<? super UUID, ? super T, ? extends T> remappingFunction) {
        return map.computeIfPresent(key, remappingFunction);
    }

    public T compute(UUID key, @NotNull BiFunction<? super UUID, ? super T, ? extends T> remappingFunction) {
        return map.compute(key, remappingFunction);
    }

    public T merge(UUID key, @NotNull T value, @NotNull BiFunction<? super T, ? super T, ? extends T> remappingFunction) {
        return map.merge(key, value, remappingFunction);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<UUID, T> entry : this.entrySet()) {
            String uuid = entry.getKey().toString();
            T value = entry.getValue();
            result.put(uuid, value);
        }


        return result;
    }

    public static <T> UUIDMap<T> deserialize(Map<String, Object> map) {
        UUIDMap<T> result = new UUIDMap<>();
        map.remove("==");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String uuid = entry.getKey();
            T value1 = (T) entry.getValue();

            result.put(UUID.fromString(uuid), value1);
        }

        return result;
    }
}