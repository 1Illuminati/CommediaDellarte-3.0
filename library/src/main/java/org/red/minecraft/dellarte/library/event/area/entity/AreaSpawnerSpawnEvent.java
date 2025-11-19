package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaSpawnerSpawnEvent extends AreaEntityEvent<SpawnerSpawnEvent> {
    public AreaSpawnerSpawnEvent(Area area, SpawnerSpawnEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaSpawnerSpawnEvent.class, k -> new HandlerList());
    }
}
