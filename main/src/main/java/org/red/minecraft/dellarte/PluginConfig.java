package org.red.minecraft.dellarte;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public enum PluginConfig {
    VERSION("config-version"),
    VAULT_ENABLE("vault.enable"),
    VAULT_FORMAT("vault.format"),
    VAULT_FRACTIONAL("vault.fractional-digits"),
    DEBUG("debug");

    private final String path;
    private Object value;

    PluginConfig(String path) {
        this.path = path;
    }

    public String asStringValue() {
        return value.toString();
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
