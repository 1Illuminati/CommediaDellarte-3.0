package org.red.library.world;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.library.util.A_DataHolder;

import java.util.List;

public interface Area {
    @NotNull
    World getWorld();

    @NotNull
    A_World getAWorld();

    @NotNull
    NamespacedKey getKey();

    boolean contain(Vector vec);

    boolean contain(Vector start, Vector end);

    boolean contain(Location vec);

    boolean contain(Location start, Location end);

    boolean contain(Area area);

    boolean contain(BoundingBox boundingBox);

    boolean overlap(Vector start, Vector end);

    boolean overlap(Location start, Location end);

    boolean overlap(Area area);

    boolean overlap(BoundingBox boundingBox);

    boolean isEventEnable();

    /**
     * if this class is a_world class the bounding box will be null
     */
    @Nullable
    BoundingBox getBoundingBox();

    @NotNull
    List<Entity> getEntities();

    @NotNull
    List<LivingEntity> getLivingEntities();
}
