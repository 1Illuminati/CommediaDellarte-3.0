package org.red.minecraft.dellarte.library.data.serialize;

import org.bukkit.util.Vector;
import org.red.library.data.DataMap;
import org.red.library.data.serialize.RegisterSerializable;

public class VectorSerialize implements RegisterSerializable<Vector> {

    @Override
    public Vector deserialize(DataMap arg0) {
        return Vector.deserialize(arg0.getMap());
    }

    @Override
    public DataMap serialize(Vector arg0) {
        DataMap result = new DataMap();
        result.getMap().putAll(arg0.serialize());
        return result;
    }

}
