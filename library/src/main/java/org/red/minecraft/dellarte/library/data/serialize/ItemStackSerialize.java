package org.red.minecraft.dellarte.library.data.serialize;

import org.bukkit.inventory.ItemStack;
import org.red.library.data.DataMap;
import org.red.library.data.serialize.RegisterSerializable;
public class ItemStackSerialize implements RegisterSerializable<ItemStack> {

    @Override
    public ItemStack deserialize(DataMap arg0) {
        return ItemStack.deserialize(arg0.getMap());
    }

    @Override
    public DataMap serialize(ItemStack arg0) {
        DataMap result = new DataMap();
        result.getMap().putAll(arg0.serialize());
        return result;
    }

}
