package org.red.minecraft.dellarte.library.exception;

public class ConfigException extends RuntimeException {
    public ConfigException(String message) {
        super(message);
    }

    public static class ConfigDataNotFoundException extends ConfigException {
        public ConfigDataNotFoundException(String fieldName) {
            super(fieldName + " configData not found. The Config class must contain all schema data.");
        }
    }

    public static class ConfigIllegalDataException extends ConfigException {
        public ConfigIllegalDataException(String fieldName, Class<?> originClass) {
            super(fieldName + " configData is must be " + originClass.getName() + " type");
        }
    }
}
