package org.red;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public enum Config {
    VERSION("config-version"),
    VAULT("enable-vault"),
    DEBUG("debug");

    private final String path;
    private Object value;

    Config(String path) {
        this.path = path;
    }

    public String asStringValue() {
        return (String) value;
    }

    public int asIntValue() {
        return (int) value;
    }

    public double asDoubleValue() {
        return (double) value;
    }

    public boolean asBooleanValue() {
        return (boolean) value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static void loadConfig(FileConfiguration config) {
        Arrays.stream(Config.values()).forEach(setting -> setting.setValue(config.get(setting.path)));
    }
}
