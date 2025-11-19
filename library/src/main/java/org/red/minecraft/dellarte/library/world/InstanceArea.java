package org.red.minecraft.dellarte.library.world;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.util.A_DataHolder;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;
import org.red.minecraft.dellarte.library.util.A_DataMap;

import java.util.ArrayList;
import java.util.List;

public class InstanceArea implements Area, A_DataHolder {
    private final Plugin plugin;
    private final String name;
    private final BoundingBox box;
    private final A_World world;

    public InstanceArea(String name, Plugin plugin, World world, Vector start, Vector end) {
        this.plugin = plugin;
        this.name = name;
        this.box = BoundingBox.of(start, end);
        this.world = CommediaDellarte.getAWorld(world);
    }

    @Override
    public @NotNull World getWorld() {
        return this.world.getWorld();
    }

    @Override
    public @NotNull A_World getAWorld() {
        return this.world;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return new NamespacedKey(plugin, name);
    }

    @Override
    public boolean contain(Vector vec) {
        return this.box.contains(vec);
    }

    @Override
    public boolean contain(Vector start, Vector end) {
        return this.box.contains(start, end);
    }

    @Override
    public boolean contain(Location vec) {
        World world = vec.getWorld();

        if (world != null && world.getName().contains(this.world.getName())) return false;

        return this.box.contains(vec.toVector());
    }

    @Override
    public boolean contain(Location start, Location end) {
        if (world.compareWorld(start) && world.compareWorld(end)) return false;
        return this.box.contains(start.toVector(), end.toVector());
    }

    @Override
    public boolean contain(Area area) {
        return area.getAWorld().compareWorld(this.world) && this.contain(area.getBoundingBox());
    }

    @Override
    public boolean contain(BoundingBox boundingBox) {
        return this.box.contains(boundingBox);
    }

    @Override
    public boolean overlap(Vector start, Vector end) {
        return this.box.overlaps(start, end);
    }

    @Override
    public boolean overlap(Location start, Location end) {
        if (world.compareWorld(start) && world.compareWorld(end)) return false;
        return this.box.overlaps(start.toVector(), end.toVector());
    }

    @Override
    public boolean overlap(Area area) {
        return area.getAWorld().compareWorld(this.world) && this.overlap(area.getBoundingBox());
    }

    @Override
    public boolean overlap(BoundingBox boundingBox) {
        return this.box.overlaps(boundingBox);
    }

    @Override
    public boolean isEventEnable() {
        return true;
    }

    @Override
    public @Nullable BoundingBox getBoundingBox() {
        return this.box;
    }

    @Override
    public @NotNull List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        this.world.getEntities().stream().filter(entity -> contain(entity.getLocation())).forEach(entities::add);
        return entities;
    }

    @Override
    public @NotNull List<LivingEntity> getLivingEntities() {
        List<LivingEntity> entities = new ArrayList<>();
        this.world.getEntitiesByClass(LivingEntity.class).stream().filter(entity -> contain(entity.getLocation())).forEach(entities::add);
        return entities;
    }

    @Override
    public A_DataMap getDataMap() {
        return this.getDataMap(plugin);
    }

    @Override
    public A_DataMap getDataMap(Plugin plugin) {
        return CommediaDellarte.getPluginData(plugin).getDataMap("area", name);
    }

    @Override
    public CoolTimeMap getCoolTime() {
        return this.getCoolTime(plugin);
    }

    @Override
    public CoolTimeMap getCoolTime(Plugin plugin) {
        return CommediaDellarte.getPluginData(plugin).getCoolTimeMap("area", name);
    }
}
