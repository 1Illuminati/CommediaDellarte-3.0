package org.red.minecraft.dellarte.library.util.finder;

import org.red.minecraft.dellarte.library.util.A_Util;
import org.red.minecraft.dellarte.library.util.A_DataMap;

public class DataMapHandler extends FindHandler<A_DataMap> implements HasNext {
    public DataMapHandler(A_DataMap data) {
        super(data);
    }

    @Override
    public FindHandler<?> getNext(String key) {
        if (!this.getData().containsKey(key)) return null;

        return A_Util.objToHandler(this.getData().get(key));
    }
}
