package org.red.minecraft.dellarte.library.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class A_DataMap implements ConfigurationSerializable {
    private Map<String, Object> map;

    public A_DataMap() {
        this.map = new HashMap<>();
    }

    public A_DataMap(Map<String, Object> map) {
        this();
        this.map.putAll(map);
    }

    public void copy(A_DataMap dataMap) {
        this.map = dataMap.getMap();
    }

    public void copy(Map<String, Object> map) {
        this.map = map;
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public int getInt(String key, int nullValue) {
        return (Integer)this.get(key, nullValue);
    }

    public void addInt(String key, int value) {
        this.put(key, this.getInt(key) + value);
    }

    public double getDouble(String key) {
        return this.getDouble(key, (double)0.0F);
    }

    public double getDouble(String key, double nullValue) {
        return (Double)this.get(key, nullValue);
    }

    public void addDouble(String key, double value) {
        this.put(key, this.getDouble(key) + value);
    }

    public String getString(String key) {
        return this.getString(key, "");
    }

    public String getString(String key, String nullValue) {
        Object obj = this.get(key, nullValue);
        return obj == null ? "" : obj.toString();
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean nullValue) {
        return (Boolean)this.get(key, nullValue);
    }

    public Object get(String key) {
        return this.get(key, (Object)null);
    }

    public Object get(String key, Object nullValue) {
        if (key.contains(".")) {
            throw new IllegalArgumentException("key cannot contain '.'");
        } else {
            if (!this.map.containsKey(key)) {
                this.put(key, nullValue);
            }

            return this.map.get(key);
        }
    }

    public <T> T getClass(String key, Class<T> clazz) {
        return (T)this.getClass(key, clazz, (Object)null);
    }

    public <T> T getClass(String key, Class<T> clazz, Object nullValue) {
        if (!this.map.containsKey(key)) {
            this.put(key, nullValue);
        }

        return (T)clazz.cast(this.map.get(key));
    }

    public <T> List<T> getList(String key) {
        return this.<T>getList(key, new ArrayList());
    }

    public <T> List<T> getList(String key, List<T> nullValue) {
        return (List)this.get(key, nullValue);
    }

    public A_DataMap getDataMap(String key) {
        return this.getDataMap(key, new A_DataMap());
    }

    public A_DataMap getDataMap(String key, A_DataMap nullValue) {
        return (A_DataMap)this.get(key, nullValue);
    }

    public UUID getUUID(String key) {
        return this.getUUID(key, UUID.randomUUID());
    }

    public UUID getUUID(String key, UUID nullValue) {
        return (UUID)this.get(key, nullValue);
    }

    public void put(String key, Object value) {
        if (key.contains(".")) {
            throw new IllegalArgumentException("key cannot contain '.'");
        } else {
            this.map.put(key, value);
        }
    }

    public A_DataMap set(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public void remove(String key) {
        this.map.remove(key);
    }

    public boolean containsKey(String key) {
        return this.map.containsKey(key);
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public Set<String> keySet() {
        return this.map.keySet();
    }

    public Collection<Object> values() {
        return this.map.values();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    public String toString() {
        return this.map.toString();
    }

    public void clear() {
        this.map.clear();
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
        Object current = this.map;
        Object result = null;

        for (String str : strs) {
            if (current == null) break;
            else if (current instanceof Map map) {
                try {
                    result = map.get(str);
                } catch (ClassCastException e) {
                    return null;
                }
            }
            else if (current instanceof List<?> list) {
                try {
                    int index = Integer.parseInt(str);
                    if (index < 0 || index >= list.size()) return null;
                    result = list.get(index);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            else result = null;

            current = result;
            if (current instanceof ConfigurationSerializable serializable)
                current = serializable.serialize();
        }

        return result;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return this.map;
    }

    public static A_DataMap deserialize(Map<String, Object> map) {
        return new A_DataMap(map);
    }
}
