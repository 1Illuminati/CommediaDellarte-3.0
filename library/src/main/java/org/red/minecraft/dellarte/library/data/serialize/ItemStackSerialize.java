package org.red.minecraft.dellarte.library.data.serialize;

import org.bukkit.inventory.ItemStack;
import org.red.library.data.DataMap;
import org.red.library.data.serialize.RegisterSerializable;

public class ItemStackSerialize implements RegisterSerializable {

    @Override
    public Object deserialize(DataMap arg0) {
        return ItemStack.deserialize(arg0.getMap());
    }

    @Override
    public DataMap serialize(Object arg0) {
        ItemStack item = (ItemStack) arg0;
        DataMap result = new DataMap();
        result.getMap().putAll(item.serialize());
        return result;
    }

}
