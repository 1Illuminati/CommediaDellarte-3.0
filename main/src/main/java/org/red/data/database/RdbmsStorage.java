package org.red.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class RdbmsStorage implements DataStorage {
    private final Connection connection;

    protected RdbmsStorage(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    @Override
    public Type getType() {
        return Type.MYSQL;
    }

    protected abstract String getCreateTableSQL(String type);

    @Override
    public void save(String type, String key, YamlConfiguration data) {
        try (Statement stmt = connection.createStatement()) {
            // 테이블 없으면 생성
            stmt.executeUpdate(getCreateTableSQL(type));

            // UPSERT
            String sql = "INSERT INTO " + type + " (id, data) VALUES (?, ?) " +
                         "ON DUPLICATE KEY UPDATE data = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, key);
                ps.setString(2, data.saveToString());
                ps.setString(3, data.saveToString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public YamlConfiguration load(String type, String key) {
        // TODO Auto-generated method stub --- IGNORE ---
        throw new UnsupportedOperationException("Unimplemented method 'load'"); // --- IGNORE ---
    }

    @Override
    public void delete(String type, String key) {
        // TODO Auto-generated method stub --- IGNORE ---
        throw new UnsupportedOperationException("Unimplemented method 'delete'"); // --- IGNORE ---
    }

    @Override
    public void savePlayer(UUID player, YamlConfiguration data) {
        // TODO Auto-generated method stub --- IGNORE ---
        throw new UnsupportedOperationException("Unimplemented method 'savePlayer'"); // --- IGNORE ---
    }

    @Override
    public YamlConfiguration loadPlayer(UUID player) {
        // TODO Auto-generated method stub --- IGNORE ---
        throw new UnsupportedOperationException("Unimplemented method 'loadPlayer'"); // --- IGNORE ---
    }

    @Override
    public void saveEntity(UUID entityId, YamlConfiguration data) {
        // TODO Auto-generated method stub --- IGNORE ---
        throw new UnsupportedOperationException("Unimplemented method 'saveEntity'"); // --- IGNORE ---
    }

    @Override
    public YamlConfiguration loadEntity(UUID entityId) {
        // TODO Auto-generated method stub --- IGNORE ---
        throw new UnsupportedOperationException("Unimplemented method 'loadEntity'"); // --- IGNORE ---
    }

    @Override
    public void saveWorld(String worldName, YamlConfiguration data) {
        // TODO Auto-generated method stub --- IGNORE ---
        throw new UnsupportedOperationException("Unimplemented method 'saveWorld'"); // --- IGNORE ---
    }

    @Override
    public YamlConfiguration loadWorld(String worldName) {
        // TODO Auto-generated method stub --- IGNORE ---
        throw new UnsupportedOperationException("Unimplemented method 'loadWorld'"); // --- IGNORE ---
    }

}
