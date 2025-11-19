package org.red.minecraft.dellarte.library.data.serialize;

import org.bukkit.util.BoundingBox;
import org.red.library.data.DataMap;
import org.red.library.data.serialize.RegisterSerializable;

public class BoundingBoxSerialize implements RegisterSerializable {

    @Override
    public Object deserialize(DataMap arg0) {
        return BoundingBox.deserialize(arg0.getMap());
    }

    @Override
    public DataMap serialize(Object arg0) {
        BoundingBox box = (BoundingBox) arg0;
        DataMap result = new DataMap();
        result.getMap().putAll(box.serialize());
        return result;
    }
}
