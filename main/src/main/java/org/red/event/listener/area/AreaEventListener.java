package org.red.event.listener.area;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.red.CommediaDellartePlugin;
import org.red.event.listener.DellarteListener;
import org.red.library.event.area.AreaEvent;
import org.red.library.world.Area;
import org.red.world.A_WorldImpl;

import java.util.HashSet;
import java.util.Set;

public abstract class AreaEventListener<T extends Event> extends DellarteListener {
    private final Class<? extends AreaEvent<T>> areaEventClass;

    public AreaEventListener(Class<? extends AreaEvent<T>> clazz) {
        areaEventClass = clazz;
    }

    public Class<? extends AreaEvent<T>> getAreaEventClass() {
        return this.areaEventClass;
    }

    public abstract void onEvent(T event);

    public void runAreaEvent(@NotNull T event, Location... locations) {
        Set<Area> areas = new HashSet<>();

        for (Location loc : locations) {
            A_WorldImpl aWorld = CommediaDellartePlugin.manager.getAWorld(loc.getWorld());
            areas.addAll(aWorld.getAllArea());
        }

        areas.forEach(area -> {
            AreaEvent<T> areaEvent;

            try {
                areaEvent = areaEventClass.getConstructor(Area.class, areaEventClass).newInstance(area, event);
                if (area.isEventEnable()) Bukkit.getPluginManager().callEvent(areaEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void runAreaEvent(@NotNull AreaEvent<T> areaEvent, Location... locations) {
        Set<Area> areas = new HashSet<>();

        for (Location loc : locations) {
            A_WorldImpl aWorld = CommediaDellartePlugin.manager.getAWorld(loc.getWorld());
            areas.addAll(aWorld.getAllArea());
        }

        areas.forEach(area -> {
            if (area.isEventEnable()) Bukkit.getPluginManager().callEvent(areaEvent);
        });
    }

    @Override
    public void register() {
        boolean isEnabled = CommediaDellartePlugin.instance.getConfig().getBoolean("area-event." +
                this.getAreaEventClass().getName().replaceAll("area", "") + ".enabled", true);

        if (isEnabled)
            super.register();
    }

}
