package org.red.minecraft.dellarte.data.adapter;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.library.data.AdapterFactory;
import org.red.minecraft.dellarte.library.data.IDataAdapter;
import org.red.minecraft.dellarte.library.exception.KeyNotFoundException;
import org.red.minecraft.dellarte.library.util.config.Config;
import org.red.minecraft.dellarte.library.util.config.IConfigSchema;
import org.red.minecraft.dellarte.library.util.map.NamespaceMap;

public class AdapterFactoryManager {
    private final NamespaceMap<AdapterFactory> factoryMap = new NamespaceMap<>();

    @Nullable
    public AdapterFactory get(NamespacedKey key) {
        return this.factoryMap.getOrDefault(key, null);
    }

    public void set(AdapterFactory factory) {
        this.factoryMap.put(factory.getKey(), factory);
    }

    public boolean contain(NamespacedKey key) {
        return this.factoryMap.containsKey(key);
    }

    public IDataAdapter createAdapter(NamespacedKey key, Config config) {
        if (!contain(key)) throw new KeyNotFoundException(key.toString());

        AdapterFactory factory = this.factoryMap.get(key);
        return factory.createAdapter(config);
    }

    public IConfigSchema getFactorySchema(NamespacedKey key) {
        if (!contain(key)) throw new KeyNotFoundException(key.toString());

        return this.factoryMap.get(key).getConfigSchema();
    }
}
