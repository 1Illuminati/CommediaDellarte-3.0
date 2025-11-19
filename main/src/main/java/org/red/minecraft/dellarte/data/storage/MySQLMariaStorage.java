package org.red.minecraft.dellarte.data.storage;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

public class MySQLMariaStorage extends RdbmsStorage {

    private final boolean mysqlormaria;

    /**
     * maria와 mysql은 똑같기 때문에 같은 클래스로 처리
     * plugin별로 별도 DB를 만들고, 각 DB 안에는 data_entry 테이블만 둔다.
     *
     * @param url jdbc:mysql://localhost:3306/ 같은 베이스 URL (DB 이름 제외)
     * @param user DB 계정
     * @param password DB 비밀번호
     * @param mysqlormaria true = mysql, false = mariadb
     */
    public MySQLMariaStorage(String host, String database, int port, String user, String password, boolean mysqlormaria) throws SQLException {
        super(host, database, port, user, password);
        this.mysqlormaria = mysqlormaria;
    }

    @Override
    public void save(String plugin, String type, String uuid, YamlConfiguration data) {
        try (Statement stmt = super.getConntection().createStatement()) {
            stmt.executeUpdate(getCreateTableSQL(plugin));

            String sql = "INSERT INTO " + plugin + " (type, uuid, data) VALUES (?, ?, ?) "
                       + "ON DUPLICATE KEY UPDATE data = ?";
            try (PreparedStatement ps = super.getConntection().prepareStatement(sql)) {
                ps.setString(1, type);
                ps.setString(2, uuid);
                ps.setString(3, data.saveToString());
                ps.setString(4, data.saveToString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public YamlConfiguration load(String plugin, String type, String uuid) {
        try {
            String sql = "SELECT data FROM " + plugin + " WHERE type = ? AND uuid = ?";
            try (PreparedStatement ps = super.getConntection().prepareStatement(sql)) {
                ps.setString(1, type);
                ps.setString(2, uuid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return YamlConfiguration.loadConfiguration(
                            new StringReader(rs.getString("data")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new YamlConfiguration();
    }

    @Override
    public void delete(String plugin, String type, String uuid) {
        try {
            String sql = "DELETE FROM  " + plugin + "  WHERE type = ? AND uuid = ?";
            try (PreparedStatement ps = super.getConntection().prepareStatement(sql)) {
                ps.setString(1, type);
                ps.setString(2, uuid);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean contain(String plugin, String type, String uuid) {
        try {
            String sql = "SELECT 1 FROM  " + plugin + "  WHERE type = ? AND uuid = ? LIMIT 1";
            try (PreparedStatement ps = super.getConntection().prepareStatement(sql)) {
                ps.setString(1, type);
                ps.setString(2, uuid);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, YamlConfiguration> loadAll(String plugin, String type) {
        Map<String, YamlConfiguration> out = new HashMap<>();
        String sql = "SELECT uuid, data FROM " + plugin + " WHERE type = ?";

        try (PreparedStatement ps = super.getConntection().prepareStatement(sql)) {

            ps.setString(1, type);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    String yaml = rs.getString("data");

                    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new StringReader(yaml));
                    out.put(uuid, cfg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }

    @Override
    public Type getType() {
        return mysqlormaria ? Type.MYSQL : Type.MARIADB;
    }

    @Override
    protected String getCreateTableSQL(String plugin) {
        return "CREATE TABLE IF NOT EXISTS " + plugin + " ("
                + "type VARCHAR(255) NOT NULL, "
                + "uuid VARCHAR(36) NOT NULL, "
                + "data TEXT NOT NULL, "
                + "PRIMARY KEY (type, uuid)"
                + ")";
    }

    @Override
    protected String getUrl(String host, String database, int port) {
        return String.format("jdbc:%s://%s:%d/%s?useSSL=false&serverTimezone=UTC", mysqlormaria ? "mysql" : "mariadb", host, port, database);
    }
}

