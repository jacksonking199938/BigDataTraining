package com.example.demo.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface InfoService {
    public List<String> findAllDatabases(Connection connection) throws SQLException;
    public List<String> findAllTables(Connection connection) throws  SQLException;
    public  List<String> findAllColumns(Connection connection, String tableName) throws  SQLException;
}
