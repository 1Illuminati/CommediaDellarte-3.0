package org.red.library.util.persistent;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BooleanPersistentDataType implements PersistentDataType<Byte, Boolean> {
    public static final BooleanPersistentDataType INSTANCE = new BooleanPersistentDataType();
    @NotNull
    @Override
    public Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @NotNull
    @Override
    public Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @NotNull
    @Override
    public Byte toPrimitive(@NotNull Boolean aBoolean, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return aBoolean ? (byte) 1 : (byte) 0;
    }

    @NotNull
    @Override
    public Boolean fromPrimitive(@NotNull Byte aByte, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return aByte >= 1;
    }
}
