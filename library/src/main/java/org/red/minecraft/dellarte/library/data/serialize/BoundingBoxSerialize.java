package org.red.minecraft.dellarte.library.data.serialize;

import org.bukkit.util.BoundingBox;
import org.red.library.data.DataMap;
import org.red.library.data.serialize.RegisterSerializable;

public class BoundingBoxSerialize implements RegisterSerializable<BoundingBox> {

    @Override
    public BoundingBox deserialize(DataMap arg0) {
        return BoundingBox.deserialize(arg0.getMap());
    }

    @Override
    public DataMap serialize(BoundingBox arg0) {
        DataMap result = new DataMap();
        result.getMap().putAll(arg0.serialize());
        return result;
    }
}
