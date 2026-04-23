package org.red.minecraft.dellarte.data.serialize;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.red.library.data.DataMap;
import org.red.minecraft.dellarte.library.data.serializable.RegisterConfigSerializable;
import org.red.minecraft.dellarte.library.data.serializable.RegisterSerializable;
import org.red.minecraft.dellarte.util.ConvertUtil;

public class A_RegisterSerializable<T> implements org.red.library.data.serialize.RegisterSerializable<T> {
    private final RegisterSerializable<T> registerSerializable;

    public A_RegisterSerializable(RegisterSerializable<T> registerSerializable) {
        this.registerSerializable = registerSerializable;
    }

    public Class<T> getType() {
        return this.registerSerializable.getType();
    }

    @Override
    public DataMap serialize(T t) {
        return ConvertUtil.convertADataMap(registerSerializable.serialize(t));
    }

    @Override
    public T deserialize(DataMap dataMap) {
        return registerSerializable.deserialize(ConvertUtil.convertDataMap(dataMap));
    }

    public static <T extends ConfigurationSerializable> A_RegisterSerializable<T> configToRegSerializable(Class<T> clazz) {
        return new A_RegisterSerializable<>(new RegisterConfigSerializable<>(clazz));
    }
}
