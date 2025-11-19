package org.red.minecraft.dellarte.library.util.finder;

import org.jetbrains.annotations.Nullable;

public interface HasNext {
    @Nullable
    FindHandler<?> getNext(String key);
}
