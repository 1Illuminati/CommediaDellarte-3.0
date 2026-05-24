package org.red.minecraft.dellarte.compatibility.dataServer.adapter;

import org.red.library.data.adapter.DatabaseAdapter;
import org.red.library.data.adapter.IAdapter;
import org.red.library.data.exception.KeyNotFoundException;
import org.red.library.data.serialize.SerializeDataMap;
import org.red.minecraft.dellarte.compatibility.dataServer.A_DataClient;

import java.util.Set;

public class DataServerAdapter implements IAdapter {
    public DataServerAdapter(A_DataClient client) {

    }

    @Override
    public SerializeDataMap loadDataMap(String s) throws KeyNotFoundException {
        return null;
    }

    @Override
    public void saveDataMap(String s, SerializeDataMap serializeDataMap) {

    }

    @Override
    public boolean containDataMap(String s) {
        return false;
    }

    @Override
    public void deleteDataMap(String s) {

    }

    @Override
    public Set<String> loadAllKey() {
        return Set.of();
    }
}
