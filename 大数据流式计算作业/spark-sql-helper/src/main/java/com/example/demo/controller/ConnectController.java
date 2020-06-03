package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
public class ConnectController {

    @RequestMapping("/connect")
    @ResponseBody
    public List<String> connect(HttpServletRequest request){
        List<String> result = new ArrayList<>();
        try{
//            String url = request.getParameter("url");
//            String user = request.getParameter("user");
//            String password = request.getParameter(("password"));
//            String driver = request.getParameter("driver");
            String url = "jdbc:hive2://bigdata37.depts.bingosoft.net:22237/user29_db";
            String user = "user29";
            String password = "pass@bingo29";
            String driver = "org.apache.hive.jdbc.HiveDriver";
            Properties properties = new Properties();
            properties.setProperty("driveClassName", driver);
            properties.setProperty("user",user);
            properties.setProperty("password",password);
            Connection connection = DriverManager.getConnection(url,properties);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("show tables");
            System.out.println(resultSet.getFetchSize());
            try{
                while(resultSet.next()) {
                    String tableName = resultSet.getString(1);
                    result.add(tableName);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            resultSet.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
