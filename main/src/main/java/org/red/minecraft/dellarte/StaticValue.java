package org.red.minecraft.dellarte;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

/**
 * 고정적인 값들 모아놓는 곳
 */
public class StaticValue {
    public static NamespacedKey getPlayerDataKey(Plugin plugin) {
        return new NamespacedKey(plugin, "player");
    }

    public static NamespacedKey getWorldDataKey(Plugin plugin) {
        return new NamespacedKey(plugin, "world");
    }

    public static NamespacedKey getEntityDataKey(Plugin plugin) {
        return new NamespacedKey(plugin, "entity");
    }
}
