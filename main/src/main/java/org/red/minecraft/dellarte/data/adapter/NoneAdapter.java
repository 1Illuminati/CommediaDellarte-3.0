package org.red.minecraft.dellarte.data.adapter;

import java.util.HashMap;
import java.util.Set;

import org.red.minecraft.dellarte.library.data.IDataAdapter;
import org.red.minecraft.dellarte.library.util.A_DataMap;

public class NoneAdapter implements IDataAdapter {
    private final HashMap<String, A_DataMap> map = new HashMap<>();

    @Override
    public boolean containDataMap(String arg0) {
        return true;
    }

    @Override
    public void deleteDataMap(String arg0) {
        map.remove(arg0);
    }

    @Override
    public Set<String> loadAllKey() {
        return map.keySet();
    }

    @Override
    public A_DataMap loadDataMap(String var1) {
        return map.computeIfAbsent(var1, key -> new A_DataMap());
    }

    @Override
    public void saveDataMap(String var1, A_DataMap var2) {
        map.put(var1, var2);
    }
}
