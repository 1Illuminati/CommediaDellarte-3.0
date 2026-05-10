package org.red.minecraft.dellarte.library.data;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.red.minecraft.dellarte.library.util.config.Config;

public record SaveConfig(NamespacedKey nameSpace, boolean autoSaveEnable, NamespacedKey saveType, int autoSaveTime, Config adapterConfig) implements Keyed {

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.nameSpace;
    }
}
