package org.red.minecraft.dellarte.library.data.serializable;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.red.minecraft.dellarte.library.util.A_DataMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RegisterConfigSerializable<T extends ConfigurationSerializable> implements RegisterSerializable<T> {
    private final Class<T> clazz;

    public RegisterConfigSerializable(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getType() {
        return this.clazz;
    }

    @Override
    public A_DataMap serialize(T var1) {
        return new A_DataMap(var1.serialize());
    }

    @Override
    public T deserialize(A_DataMap var1) {
        T result;
        try {
            Method method = clazz.getDeclaredMethod("deserialize", Map.class);
            result = (T) method.invoke(null, var1.getMap());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
