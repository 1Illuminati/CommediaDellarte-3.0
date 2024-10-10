package org.red.library.util.finder;

import org.red.library.util.A_Util;
import org.red.library.util.DataMap;

public class DataMapHandler extends FindHandler<DataMap> implements HasNext {
    public DataMapHandler(DataMap data) {
        super(data);
    }

    @Override
    public FindHandler<?> getNext(String key) {
        if (!this.getData().containsKey(key)) return null;

        return A_Util.objToHandler(this.getData().get(key));
    }
}
