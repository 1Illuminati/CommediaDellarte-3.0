package org.red.minecraft.dellarte.library.util;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record PairData<T, V>(T dataA, V dataB) implements ConfigurationSerializable {

    public boolean equalsDataA(T dataA) {
        return this.dataA.equals(dataA);
    }

    public boolean equalsDataB(V dataB) {
        return this.dataA.equals(dataB);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("dataA", dataA);
        result.put("dataB", dataB);

        return result;
    }

    public static <T, V> PairData<T, V> deserialize(Map<String, Object> map) {
        return new PairData<>((T) map.get("dataA"), (V) map.get("dataB"));
    }
}
