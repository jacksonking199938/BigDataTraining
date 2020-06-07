package com.example.demo.serviceImpl;

import com.example.demo.service.InfoService;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class InfoServiceIml implements InfoService {

    public List<String> findAllDatabases(Connection connection) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("show databases");
        List<String> databases = new ArrayList<>();
        while(resultSet.next()){
            String database = resultSet.getString(1);
            System.out.println(database);
            databases.add(database);
        }
        statement.close();
        return databases;
    }
    
    public List<String> findAllTables(Connection connection) throws  SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("show tables");
        List<String> tableNames = new ArrayList<>();
        while(resultSet.next()){
            String tableName = resultSet.getString(1);
            System.out.println(tableName);
            tableNames.add(tableName);
        }
        statement.close();
        return  tableNames;
    }

    public  List<String> findAllColumns(Connection connection, String tableName) throws  SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("show columns from "+tableName);
        List<String> columnNames = new ArrayList<>();
        while (resultSet.next()) {
            String columnName = resultSet.getString(1);
            System.out.println(columnName);
            columnNames.add(columnName);
        }
        statement.close();
        return columnNames;
    }


    
}