package org.red.library.util.finder;

import org.jetbrains.annotations.Nullable;

public interface HasNext {
    @Nullable
    FindHandler<?> getNext(String key);
}
