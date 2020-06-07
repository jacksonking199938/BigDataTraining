package com.example.demo.serviceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectService {
    public static Connection connectToSpark(String url, String user, String password) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("driveClassName", "org.apache.hive.jdbc.HiveDriver");
        properties.setProperty("user",user);
        properties.setProperty("password",password);
        Connection connection = DriverManager.getConnection(url,properties);
        return connection;
    }
}
