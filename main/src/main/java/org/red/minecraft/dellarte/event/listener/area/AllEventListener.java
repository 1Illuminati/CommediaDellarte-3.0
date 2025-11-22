package org.red.minecraft.dellarte.event.listener.area;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import org.red.minecraft.dellarte.event.listener.DellarteListener;
import org.red.minecraft.dellarte.library.event.area.AreaEvent;
import org.red.minecraft.dellarte.library.world.Area;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.world.A_WorldImpl;

import java.util.HashSet;
import java.util.Set;

public abstract class AllEventListener<T extends Event> extends DellarteListener implements EventExecutor {
    private final Class<? extends AreaEvent<T>> areaEventClass;
    private final Class<T> eventClass;

    public AllEventListener(Class<? extends AreaEvent<T>> areaEventClass, Class<T> eventClass) {
        this.areaEventClass = areaEventClass;
        this.eventClass = eventClass;
    }

    public AllEventListener(Class<? extends AreaEvent<T>> areaEventClass, String classPath) throws ClassNotFoundException {
        this.areaEventClass = areaEventClass;
        Class<?> clazz = Class.forName(classPath);
        this.eventClass = (Class<T>) clazz;
    }

    public Class<? extends AreaEvent<T>> getAreaEventClass() {
        return this.areaEventClass;
    }

    @EventHandler
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

        if (!isEnabled) return;

        Bukkit.getPluginManager().registerEvent(eventClass, this, EventPriority.NORMAL, this, CommediaDellartePlugin.instance);
        CommediaDellartePlugin.sendDebugLog("Register Area Event - " + areaEventClass.getName());
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        if (!eventClass.isAssignableFrom(event.getClass())) return;

        this.onEvent(eventClass.cast(event));
    }
}
