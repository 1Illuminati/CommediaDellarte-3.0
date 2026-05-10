package org.red.minecraft.dellarte;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public enum PluginConfig {
    VERSION("config-version"),
    ENABLE_VAULT("enable-vault"),
    VAULT_FORMAT("vault-format"),
    DEBUG("debug");

    private final String path;
    private Object value;

    PluginConfig(String path) {
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
        Arrays.stream(PluginConfig.values()).forEach(setting -> setting.setValue(config.get(setting.path)));
    }
}
