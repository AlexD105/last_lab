package com.lab7.api.dao;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Getter @Setter
public class JdbcConfig {

    static {
        try {
            Class.forName(PropertyReader.getPropValue("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection connection;

    public JdbcConfig() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", PropertyReader.getPropValue("user"));
        properties.setProperty("password", PropertyReader.getPropValue("password"));

        String url = PropertyReader.getPropValue("url");

        this.connection = DriverManager.getConnection(url, properties);
    }
}
