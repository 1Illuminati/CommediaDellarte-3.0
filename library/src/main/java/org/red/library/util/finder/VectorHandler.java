package org.red.library.util.finder;

import org.bukkit.util.Vector;

public class VectorHandler extends FindHandler<Vector> implements HasNext {
    public VectorHandler(Vector data) {
        super(data);
    }

    @Override
    public FindHandler<?> getNext(String key) {
        return switch (key) {
            case "X" -> new FindHandler<>(getData().getX());
            case "Y" -> new FindHandler<>(getData().getY());
            case "Z" -> new FindHandler<>(getData().getZ());
            default -> null;
        };

    }
}
