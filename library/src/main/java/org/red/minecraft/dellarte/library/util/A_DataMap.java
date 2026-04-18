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

import javax.xml.crypto.Data;
import java.util.*;

public class A_DataMap extends DataMap {

    public static A_DataMap convert(DataMap dataMap) {
        A_DataMap result = new A_DataMap();
        result.copy(dataMap);
        return result;
    }

    @Override
    public void copy(DataMap dataMap) {
        super.copy(dataMap);
    }

    @Override
    public void copy(Map<String, Object> map) {
        super.copy(map);
    }

    @Override
    public int getInt(String key) {
        return super.getInt(key);
    }

    @Override
    public int getInt(String key, int nullValue) {
        return super.getInt(key, nullValue);
    }

    @Override
    public void addInt(String key, int value) {
        super.addInt(key, value);
    }

    @Override
    public double getDouble(String key) {
        return super.getDouble(key);
    }

    @Override
    public double getDouble(String key, double nullValue) {
        return super.getDouble(key, nullValue);
    }

    @Override
    public void addDouble(String key, double value) {
        super.addDouble(key, value);
    }

    @Override
    public String getString(String key) {
        return super.getString(key);
    }

    @Override
    public String getString(String key, String nullValue) {
        return super.getString(key, nullValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return super.getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean nullValue) {
        return super.getBoolean(key, nullValue);
    }

    @Override
    public Object get(String key) {
        return super.get(key);
    }

    @Override
    public Object get(String key, Object nullValue) {
        return super.get(key, nullValue);
    }

    @Override
    public <T> T getClass(String key, Class<T> clazz) {
        return super.getClass(key, clazz);
    }

    @Override
    public <T> T getClass(String key, Class<T> clazz, Object nullValue) {
        return super.getClass(key, clazz, nullValue);
    }

    @Override
    public <T> List<T> getList(String key) {
        return super.getList(key);
    }

    @Override
    public <T> List<T> getList(String key, List<T> nullValue) {
        return super.getList(key, nullValue);
    }

    @Override
    public A_DataMap getDataMap(String key) {
        return A_DataMap.convert(super.getDataMap(key));
    }

    @Override
    public DataMap getDataMap(String key, DataMap nullValue) {
        return super.getDataMap(key, nullValue);
    }

    @Override
    public UUID getUUID(String key) {
        return super.getUUID(key);
    }

    @Override
    public UUID getUUID(String key, UUID nullValue) {
        return super.getUUID(key, nullValue);
    }

    @Override
    public void put(String key, Object value) {
        super.put(key, value);
    }

    @Override
    public A_DataMap set(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public void remove(String key) {
        super.remove(key);
    }

    @Override
    public boolean containsKey(String key) {
        return super.containsKey(key);
    }

    @Override
    public Map<String, Object> getMap() {
        return super.getMap();
    }

    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<Object> values() {
        return super.values();
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return super.entrySet();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void clear() {
        super.clear();
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

    @Nullable
    public Object finder(String path) {
        String[] strs = path.split("\\.");

        FindHandler<?> lastFinder = A_Util.objToHandler(strs[0]);
        for (int index = 1; lastFinder != null && lastFinder.hasNext() && strs.length > index; index++) {
            lastFinder = ((HasNext) lastFinder).getNext(strs[index]);
        }

        return lastFinder == null ? null : lastFinder.getData();
    }

    public static A_DataMap deserialize(DataMap map) {
        A_DataMap result = new A_DataMap();
        result.getMap().putAll(map.getMap());
        return result;
    }
}
