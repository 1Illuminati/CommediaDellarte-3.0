package org.red.minecraft.dellarte.library.exception;

import org.bukkit.NamespacedKey;

public class DataStorageNullException extends RuntimeException {
    public DataStorageNullException(NamespacedKey key) {
        super(key + " is a non-existent DataStorage");
    }
}
