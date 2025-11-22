package org.red.minecraft.dellarte.data;

import org.bukkit.NamespacedKey;
import org.red.minecraft.dellarte.exception.DataStorageNullException;
import org.red.minecraft.dellarte.library.util.NamespaceMap;

public class StorageManager {
    private final NamespaceMap<DataStorage> map = new NamespaceMap<>();

    public DataStorage getStorage(NamespacedKey key) {
        if (!map.containsKey(key)) throw new DataStorageNullException(key);

        return map.get(key);
    }

    public boolean containStorage(NamespacedKey key) {
        return map.containsKey(key);
    }
}
