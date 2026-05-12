package org.red.minecraft.dellarte.data.adapter;

import org.bukkit.NamespacedKey;
import org.red.library.data.adapter.DatabaseAdapter;
import org.red.library.data.adapter.MySqlAdapter;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.library.data.AdapterFactory;
import org.red.minecraft.dellarte.library.data.IDataAdapter;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.util.PairData;
import org.red.minecraft.dellarte.library.util.config.A_ConfigSchema;
import org.red.minecraft.dellarte.library.util.config.Config;
import org.red.minecraft.dellarte.library.util.config.IConfigSchema;

import java.sql.SQLException;

public class MySqlAdapterFactory implements AdapterFactory {
    public static final NamespacedKey KEY = new NamespacedKey(CommediaDellartePlugin.instance, "mysql");
    @Override
    public IDataAdapter createAdapter(Config configData) {
        String host = configData.getDataString("host");
        String database = configData.getDataString("database");
        int port = configData.getDataInteger("port");
        String username = configData.getDataString("username");
        String password = configData.getDataString("password");
        String table = configData.getDataString("table");

        DatabaseAdapter.Config dbConfig = new DatabaseAdapter.Config(host, database, port, username, password, table);
        try {
            return new A_DataAdapter(new MySqlAdapter(dbConfig));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NamespacedKey getKey() {
        return KEY;
    }

    @Override
    public IConfigSchema getConfigSchema() {
        return new A_ConfigSchema(new PairData[]{new PairData<>("host", String.class), new PairData<>("port", Integer.class), new PairData<>("database", String.class),
                 new PairData<>("username", String.class), new PairData<>("password", String.class), new PairData<>("table", String.class)},
                new PairData[]{new PairData<>("port", 3306), new PairData<>("table", "%type%")});
    }
}
