package org.red.library.util.finder;

import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

public class BoundingBoxHandler extends FindHandler<BoundingBox> implements HasNext {
    public BoundingBoxHandler(BoundingBox data) {
        super(data);
    }

    public void test() {
        JsonObject obj = new JsonObject();
    }

    @Override
    public @Nullable FindHandler<?> getNext(String key) {
        return switch (key) {
            case "minX" -> new FindHandler<>(getData().getMinX());
            case "minY" -> new FindHandler<>(getData().getMinY());
            case "minZ" -> new FindHandler<>(getData().getMinZ());
            case "maxX" -> new FindHandler<>(getData().getMaxX());
            case "maxY" -> new FindHandler<>(getData().getMaxY());
            case "maxZ" -> new FindHandler<>(getData().getMaxZ());
            case "centerX" -> new FindHandler<>(getData().getCenterX());
            case "centerY" -> new FindHandler<>(getData().getCenterY());
            case "centerZ" -> new FindHandler<>(getData().getCenterZ());
            case "min" -> new VectorHandler(getData().getMin());
            case "max" -> new VectorHandler(getData().getMax());
            case "center" -> new VectorHandler(getData().getCenter());
            default -> null;
        };
    }
}
