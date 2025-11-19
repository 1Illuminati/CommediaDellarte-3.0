package org.red.minecraft.dellarte.library.event.area.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityEvent;
import org.red.minecraft.dellarte.library.event.area.AreaEvent;
import org.red.minecraft.dellarte.library.world.Area;

public abstract class AreaEntityEvent<T extends EntityEvent> extends AreaEvent<T> {
    private final Entity entity;
    protected AreaEntityEvent(Area area, T event) {
        super(area, event);

        this.entity = event.getEntity();
    }

    protected AreaEntityEvent(Area area, T event, Entity entity) {
        super(area, event);

        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
