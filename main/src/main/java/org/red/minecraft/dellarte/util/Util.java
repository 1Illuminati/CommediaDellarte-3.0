package org.red.minecraft.dellarte.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.red.library.data.DataMap;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.library.data.serializable.RegisterConfigSerializable;
import org.red.minecraft.dellarte.library.util.A_DataMap;

public final class Util {

    public static A_DataMap convertDataMap(DataMap map) {
        A_DataMap result = new A_DataMap();
        result.copy(map.getMap());
        return result;
    }

    public static DataMap convertADataMap(A_DataMap map) {
        DataMap result = new DataMap();
        result.copy(map.getMap());
        return result;
    }

    public static void registerCraftItemStack() {
        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        try {
            Class<? extends ConfigurationSerializable> craftItemStackClass =
                    Class.forName(pkg + ".inventory.CraftItemStack").asSubclass(ConfigurationSerializable.class);
            CommediaDellartePlugin.manager.registerSerializableClass(new RegisterConfigSerializable<>(craftItemStackClass));
        } catch (ClassNotFoundException e) {
            CommediaDellartePlugin.sendErrorLog("CraftItemStack class not found at " + pkg + ".inventory.CraftItemStack: " + e.getMessage());
        } catch (ClassCastException e) {
            CommediaDellartePlugin.sendErrorLog("CraftItemStack does not implement ConfigurationSerializable: " + e.getMessage());
        }
    }
}
