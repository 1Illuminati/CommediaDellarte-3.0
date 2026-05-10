package org.red.minecraft.dellarte.library.data;

import org.bukkit.NamespacedKey;
import org.red.minecraft.dellarte.library.util.config.Config;
import org.red.minecraft.dellarte.library.util.config.IConfigSchema;

public interface AdapterFactory {
    IDataAdapter createAdapter(Config configData);
    NamespacedKey getKey();
    IConfigSchema getConfigSchema();
}
