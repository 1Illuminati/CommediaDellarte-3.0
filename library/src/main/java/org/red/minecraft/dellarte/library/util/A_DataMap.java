package org.red.minecraft.dellarte.library.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.red.library.data.DataMap;
import org.red.minecraft.dellarte.library.util.finder.FindHandler;
import org.red.minecraft.dellarte.library.util.finder.HasNext;

public class A_DataMap extends DataMap {

    public static A_DataMap convert(DataMap dataMap) {
        A_DataMap result = new A_DataMap();
        result.copy(dataMap);
        return result;
    }

    public ItemStack getItemStack(String key) {
        return getItemStack(key, new ItemStack(Material.AIR));
    }

    public ItemStack getItemStack(String key, ItemStack nullValue) {
        return (ItemStack) this.get(key, nullValue);
    }

    public Location getLocation(String key) {
        return getLocation(key, new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
    }

    public Location getLocation(String key, Location nullValue) {
        return (Location) this.get(key, nullValue);
    }

    public Vector getVector(String key) {
        return getVector(key, new Vector(0, 0, 0));
    }

    public Vector getVector(String key, Vector nullValue) {
        return (Vector) this.get(key, nullValue);
    }

    public BoundingBox getBoundingBox(String key) {
        return this.getBoundingBox(key, new BoundingBox(0, 0, 0, 0, 0, 0));
    }

    public BoundingBox getBoundingBox(String key, BoundingBox nullValue) {
        return (BoundingBox) this.get(key, nullValue);
    }

    @Override
    public A_DataMap getDataMap(String key) {
        return this.getDataMap(key, new A_DataMap());
    }

    public A_DataMap getDataMap(String key, A_DataMap nullValue) {
        return (A_DataMap)this.get(key, nullValue);
    }

    @Override
    public A_DataMap set(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @Nullable
    public Object finder(String path) {
        String[] strs = path.split("\\.");

        FindHandler<?> lastFinder = A_Util.objToHandler(strs[0]);
        for (int index = 1; lastFinder != null && lastFinder.hasNext() && strs.length > index; index++) {
            lastFinder = ((HasNext) lastFinder).getNext(strs[index]);
        }

        return lastFinder == null ? null : lastFinder.getData();
    }
}
