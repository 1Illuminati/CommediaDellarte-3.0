package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.red.minecraft.dellarte.library.world.Area;

public class AreaVillagerCareerChangeEvent extends AreaEntityEvent<VillagerCareerChangeEvent> {
    public AreaVillagerCareerChangeEvent(Area area, VillagerCareerChangeEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaVillagerCareerChangeEvent.class, k -> new HandlerList());
    }
}
