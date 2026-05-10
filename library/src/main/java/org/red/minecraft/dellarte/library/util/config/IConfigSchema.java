package org.red.minecraft.dellarte.library.util.config;

import org.red.minecraft.dellarte.library.util.PairData;

public interface IConfigSchema {
    PairData<String, Class<?>>[] getField();
    PairData<String, Object>[] getDefault();
}
