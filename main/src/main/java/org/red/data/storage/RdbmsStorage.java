package org.red.data.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public abstract class RdbmsStorage implements DataStorage {
    private final Connection connection;

    protected RdbmsStorage(String host, String database, int port, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(getUrl(host, database, port), user, password);
    }

    protected Connection getConntection() {
        return this.connection;
    }

    protected abstract String getUrl(String host, String database, int port);

    protected abstract String getCreateTableSQL(String plugin);
}
