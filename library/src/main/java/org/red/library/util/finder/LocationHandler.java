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
            case "Yaw" -> new FindHandler<>(getData().getYaw());
            case "Pitch" -> new FindHandler<>(getData().getPitch());
            case "World" -> getData().getWorld() == null ? null : new FindHandler<>(getData().getWorld().getName());
            default -> null;
        };
    }
}
