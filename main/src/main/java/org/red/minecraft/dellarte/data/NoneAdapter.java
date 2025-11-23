package org.red.minecraft.dellarte.data;

import java.util.HashMap;
import java.util.Set;

import org.red.library.data.adapter.IAdapter;
import org.red.library.data.serialize.SerializeDataMap;

public class NoneAdapter implements IAdapter {
    private final HashMap<String, SerializeDataMap> map = new HashMap<>();

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
    public SerializeDataMap loadDataMap(String arg0) {
        return map.computeIfAbsent(arg0, getKey -> {
            return new SerializeDataMap();
        });
    }

    @Override
    public void saveDataMap(String arg0, SerializeDataMap arg1) {
        map.put(arg0, arg1);
    }

}
