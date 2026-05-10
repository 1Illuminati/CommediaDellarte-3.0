package org.red.minecraft.dellarte.data.adapter;

import org.bukkit.NamespacedKey;
import org.red.library.data.adapter.FileAdapter;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.library.data.AdapterFactory;
import org.red.minecraft.dellarte.library.data.IDataAdapter;
import org.red.minecraft.dellarte.library.util.PairData;
import org.red.minecraft.dellarte.library.util.config.A_ConfigSchema;
import org.red.minecraft.dellarte.library.util.config.Config;
import org.red.minecraft.dellarte.library.util.config.IConfigSchema;

public class FileAdapterFactory implements AdapterFactory {
    public static final NamespacedKey KEY = new NamespacedKey(CommediaDellartePlugin.instance, "file");
    @Override
    public IDataAdapter createAdapter(Config configData) {
        String directory = configData.getDataString("directory");
        return new A_DataAdapter(new FileAdapter(directory));
    }

    @Override
    public NamespacedKey getKey() {
        return KEY;
    }

    @Override
    public IConfigSchema getConfigSchema() {
        return new A_ConfigSchema(new PairData[]{new PairData<>("directory", String.class)},
                new PairData[]{new PairData<>("directory", "%type_name%")});
    }
}
