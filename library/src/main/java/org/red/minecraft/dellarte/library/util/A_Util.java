package org.red.minecraft.dellarte.library.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.red.minecraft.dellarte.library.util.finder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class A_Util {
    private A_Util() {
        throw new UnsupportedOperationException();
    }

    public static List<String> removeStringNotStartWith(List<String> list, String string) {
        if (list == null) return null;
        List<String> copyList = new ArrayList<>(list);
        list.stream().filter(s -> !s.startsWith(string)).forEach(copyList::remove);
        return copyList;
    }

    public static FindHandler<?> objToHandler(@NotNull Object obj) {
        if (obj instanceof Integer temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof Double temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof Boolean temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof String temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof Vector temp) {
            return new VectorHandler(temp);
        } else if (obj instanceof Location temp) {
            return new LocationHandler(temp);
        } else if (obj instanceof A_DataMap temp) {
            return new DataMapHandler(temp);
        } else if (obj instanceof UUID temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof CoolTimeMap temp) {
            return new CoolTimeMapHandler(temp);
        } else if (obj instanceof Class<?> temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof Long temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof Float temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof Character temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof Byte temp) {
            return new FindHandler<>(temp);
        } else if (obj instanceof BoundingBoxHandler temp) {
            return new FindHandler<>(temp);
        } else {
            return new FindHandler<>(obj);
        }
    }
}
