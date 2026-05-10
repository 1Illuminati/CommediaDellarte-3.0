package org.red.minecraft.dellarte.library.exception;

public class KeyNotFoundException extends RuntimeException {
    public KeyNotFoundException(String key) {
        super(key + " is not exist key");
    }
}
