package org.red.minecraft.dellarte.library.data.serialize;

import org.bukkit.Location;
import org.red.library.data.DataMap;
import org.red.library.data.serialize.RegisterSerializable;

public class LocationSerialize implements RegisterSerializable {

    @Override
    public Object deserialize(DataMap arg0) {
        return Location.deserialize(arg0.getMap());
    }

    @Override
    public DataMap serialize(Object arg0) {
        Location loc = (Location) arg0;
        DataMap result = new DataMap();
        result.getMap().putAll(loc.serialize());
        return result;
    }

}
