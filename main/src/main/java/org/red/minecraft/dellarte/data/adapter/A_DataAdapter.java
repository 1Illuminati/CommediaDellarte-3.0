package org.red.minecraft.dellarte.data.adapter;

import org.red.library.data.DataMap;
import org.red.library.data.adapter.IAdapter;
import org.red.library.data.exception.KeyNotFoundException;
import org.red.library.data.serialize.DataMapConverter;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.library.data.IDataAdapter;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.util.Util;

import java.util.Set;

public class A_DataAdapter implements IDataAdapter {
    private final DataMapConverter converter;
    private final IAdapter adapter;
    public A_DataAdapter(IAdapter adapter) {
        this.adapter = adapter;
        this.converter = new DataMapConverter(CommediaDellartePlugin.manager.getStorageManager());
    }

    @Override
    public A_DataMap loadDataMap(String var1) {
        try {
            return Util.convertDataMap((DataMap) converter.deserializeObject(adapter.loadDataMap(var1)));
        } catch (KeyNotFoundException e) {
            throw new org.red.minecraft.dellarte.library.exception.KeyNotFoundException(var1);
        }
    }

    @Override
    public void saveDataMap(String var1, A_DataMap var2) {
        adapter.saveDataMap(var1, converter.serializeObject(Util.convertADataMap(var2)));
    }

    @Override
    public boolean containDataMap(String var1) {
        return adapter.containDataMap(var1);
    }

    @Override
    public void deleteDataMap(String var1) {
        adapter.deleteDataMap(var1);
    }

    @Override
    public Set<String> loadAllKey() {
        return adapter.loadAllKey();
    }
}
