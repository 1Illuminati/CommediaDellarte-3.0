package org.red.minecraft.dellarte.library.data.serializable;

import org.red.minecraft.dellarte.library.util.A_DataMap;

public interface RegisterSerializable<T> {
    Class<T> getType();

    A_DataMap serialize(T var1);

    T deserialize(A_DataMap var1);
}
