package org.red.minecraft.dellarte.event.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.data.serialize.BoundingBoxSerialize;
import org.red.minecraft.dellarte.library.data.serialize.ItemStackSerialize;
import org.red.minecraft.dellarte.library.data.serialize.LocationSerialize;
import org.red.minecraft.dellarte.library.data.serialize.VectorSerialize;
import org.red.minecraft.dellarte.library.event.FirstLoadEvent;

public class FirstLoadSetting extends DellarteListener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void firstLoadEvent(FirstLoadEvent event) {
        CommediaDellarte.registerSerializableClass(ItemStack.class, new ItemStackSerialize());
        CommediaDellarte.registerSerializableClass(Location.class, new LocationSerialize());
        CommediaDellarte.registerSerializableClass(BoundingBox.class, new BoundingBoxSerialize());
        CommediaDellarte.registerSerializableClass(Vector.class, new VectorSerialize());
        CommediaDellartePlugin.manager.createDataStorages();
    }
}
