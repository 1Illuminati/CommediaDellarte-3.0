package org.red.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Test {
    private static final Logger logger = createCustomLogger();

    private static Logger createCustomLogger() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

        // 패턴 레이아웃 설정
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(config)
                .withPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n")
                .build();

        // 파일 어펜더 설정
        Appender fileAppender = FileAppender.newBuilder()
                .withFileName("C:/logs/app.log")
                .withAppend(true)
                .withName("FileLogger")
                .withLayout(layout)
                .withFilter(ThresholdFilter.createFilter(Level.INFO, Filter.Result.ACCEPT, Filter.Result.DENY))
                .setConfiguration(config)
                .build();

        fileAppender.start();

        // 콘솔 어펜더 설정
        Appender consoleAppender = ConsoleAppender.newBuilder()
                .withLayout(layout)
                .setConfiguration(config)
                .withName("ConsoleLogger")
                .build();

        consoleAppender.start();

        // 로거 설정
        config.addAppender(fileAppender);
        config.addAppender(consoleAppender);
        config.getRootLogger().addAppender(config.getAppender("FileLogger"), Level.INFO, null);
        config.getRootLogger().addAppender(config.getAppender("ConsoleLogger"), null, null);
        config.getRootLogger().setLevel(Level.DEBUG);

        ctx.updateLoggers();

        return LogManager.getLogger(Test.class);
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "commediadellarte";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "fkdnxj4693!";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // 데이터베이스와 테이블이 없는 경우 생성
            createDatabaseIfNotExists(connection);
            createTableIfNotExists(connection);

            // Map 데이터를 생성하여 삽입
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("name", "test");
            mapData.put("role", "test");
            mapData.put("skills", new HashMap<String, Object>() {{
                put("comedy", 90);
                put("acrobatics", 85);
            }});

            UUID uuid = UUID.randomUUID();
            insertPlayerData(connection, uuid, mapData);

            // 삽입된 데이터 조회
            Map<String, Object> retrievedData = getPlayerData(connection, uuid);
            System.out.println("Retrieved Data: " + retrievedData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDatabaseIfNotExists(Connection connection) throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Database checked/created: " + DB_NAME);
        }
    }

    private static void createTableIfNotExists(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + DB_NAME + ".player_data (" +
                "id VARCHAR(36) PRIMARY KEY, " +
                "data JSON NOT NULL)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Table checked/created: player_data");
        }
    }

    private static void insertPlayerData(Connection connection, UUID uuid, Map<String, Object> data) throws SQLException {
        String sql = "INSERT INTO " + DB_NAME + ".player_data (id, data) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, jsonData);

            preparedStatement.executeUpdate();
            System.out.println("Data inserted with UUID: " + uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> getPlayerData(Connection connection, UUID uuid) throws SQLException {
        String sql = "SELECT data FROM " + DB_NAME + ".player_data WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String jsonData = resultSet.getString("data");
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonData, Map.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
