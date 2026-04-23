package org.red.minecraft.dellarte.util;

import org.red.library.data.DataMap;
import org.red.minecraft.dellarte.library.util.A_DataMap;

public final class ConvertUtil {

    public static A_DataMap convertDataMap(DataMap map) {
        A_DataMap result = new A_DataMap();
        result.copy(map.getMap());
        return result;
    }

    public static DataMap convertADataMap(A_DataMap map) {
        DataMap result = new DataMap();
        result.copy(map.getMap());
        return result;
    }
}
