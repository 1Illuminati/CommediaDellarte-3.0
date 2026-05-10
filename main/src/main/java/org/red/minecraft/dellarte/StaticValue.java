package org.red.minecraft.dellarte;

import org.bukkit.NamespacedKey;

/**
 * 고정적인 값들 모아놓는 곳
 */
public class StaticValue {
    public static final NamespacedKey WORLD_DATA_KEY = new NamespacedKey(CommediaDellartePlugin.instance, "world");
    public static final NamespacedKey PLAYER_DATA_KEY = new NamespacedKey(CommediaDellartePlugin.instance, "player");
    public static final NamespacedKey ENTITY_DATA_KEY = new NamespacedKey(CommediaDellartePlugin.instance, "entity");
}
