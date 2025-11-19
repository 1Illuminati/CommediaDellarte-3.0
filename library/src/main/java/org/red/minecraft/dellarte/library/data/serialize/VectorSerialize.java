package org.red.minecraft.dellarte.library.data.serialize;

import org.bukkit.util.Vector;
import org.red.library.data.DataMap;
import org.red.library.data.serialize.RegisterSerializable;

public class VectorSerialize implements RegisterSerializable {

    @Override
    public Object deserialize(DataMap arg0) {
        return Vector.deserialize(arg0.getMap());
    }

    @Override
    public DataMap serialize(Object arg0) {
        Vector vec = (Vector) arg0;
        DataMap result = new DataMap();
        result.getMap().putAll(vec.serialize());
        return result;
    }

}
