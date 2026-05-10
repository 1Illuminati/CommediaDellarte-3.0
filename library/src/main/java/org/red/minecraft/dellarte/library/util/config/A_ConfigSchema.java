package org.red.minecraft.dellarte.library.util.config;

import org.red.minecraft.dellarte.library.util.PairData;

public record A_ConfigSchema(PairData<String, Class<?>>[] fields, PairData<String, Object>[] defaults) implements IConfigSchema {
    @Override
    public PairData<String, Class<?>>[] getField() {
        return fields;
    }

    @Override
    public PairData<String, Object>[] getDefault() {
        return defaults;
    }
}
