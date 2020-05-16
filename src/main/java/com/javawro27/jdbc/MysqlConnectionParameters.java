package com.javawro27.jdbc;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MysqlConnectionParameters {
    private final static String MYSQL_PROPERTIES_FILENAME="/jdbc.properties";

    private String username;
    private String password;
    private String databaseName;
    private String databaseHost;
    private String databasePort;

    public MysqlConnectionParameters() {
        init();
    }

    private void init() {
        try {
            Properties propertiesHolder = loadProperties();
            username = propertiesHolder.getProperty("database.jdbc.username");
            password = propertiesHolder.getProperty("database.jdbc.password");

            databaseName = propertiesHolder.getProperty("database.jdbc.name");
            databaseHost = propertiesHolder.getProperty("database.jdbc.host");
            databasePort = propertiesHolder.getProperty("database.jdbc.port");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        InputStream jdbcPropertiesStream = this.getClass().getResourceAsStream(MYSQL_PROPERTIES_FILENAME);
        if (jdbcPropertiesStream ==null){
            System.err.println("Brak pliku konfiguracyjnego w resources.");
            System.exit(99);
        }
        properties.load(jdbcPropertiesStream);
        return properties;
    }

    public static String getMysqlPropertiesFilename() {
        return MYSQL_PROPERTIES_FILENAME;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }
}
