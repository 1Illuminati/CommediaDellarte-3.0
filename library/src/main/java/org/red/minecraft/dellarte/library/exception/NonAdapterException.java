package org.red.minecraft.dellarte.library.exception;

import org.bukkit.NamespacedKey;

public class NonAdapterException extends RuntimeException {
    public NonAdapterException(NamespacedKey key) {
        super(key + " adapter is not found");
    }
}
