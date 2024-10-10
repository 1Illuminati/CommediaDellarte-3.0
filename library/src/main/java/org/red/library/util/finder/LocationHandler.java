package org.red.library.util.finder;

import org.bukkit.Location;

public class LocationHandler extends FindHandler<Location> implements HasNext {
    public LocationHandler(Location data) {
        super(data);
    }

    @Override
    public FindHandler<?> getNext(String key) {
        return switch (key) {
            case "X" -> new FindHandler<>(getData().getX());
            case "Y" -> new FindHandler<>(getData().getY());
            case "Z" -> new FindHandler<>(getData().getZ());
            case "blockX" -> new FindHandler<>(getData().getBlockX());
            case "blockY" -> new FindHandler<>(getData().getBlockY());
            case "blockZ" -> new FindHandler<>(getData().getBlockZ());
            case "yaw" -> new FindHandler<>(getData().getYaw());
            case "pitch" -> new FindHandler<>(getData().getPitch());
            case "world" -> getData().getWorld() == null ? null : new FindHandler<>(getData().getWorld().getName());
            case "vector" -> new VectorHandler(this.getData().toVector());
            default -> null;
        };
    }
}
