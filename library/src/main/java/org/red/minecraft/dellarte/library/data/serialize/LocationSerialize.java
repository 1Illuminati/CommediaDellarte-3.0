package org.red.minecraft.dellarte.library.data.serialize;

import org.bukkit.Location;
import org.red.library.data.DataMap;
import org.red.library.data.serialize.RegisterSerializable;

public class LocationSerialize implements RegisterSerializable<Location> {

    @Override
    public Location deserialize(DataMap arg0) {
        return Location.deserialize(arg0.getMap());
    }

    @Override
    public DataMap serialize(Location arg0) {
        DataMap result = new DataMap();
        result.getMap().putAll(arg0.serialize());
        return result;
    }

}
