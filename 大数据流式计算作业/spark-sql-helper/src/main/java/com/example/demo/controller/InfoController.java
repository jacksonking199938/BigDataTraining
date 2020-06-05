package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.serviceImpl.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.HttpConstraintElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Controller
public class InfoController {
    @Autowired
    InfoService infoService;

    @RequestMapping(value = "/getInfo")
    public void getInfo(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("connection");
        JSONObject jsonObject = new JSONObject();
        if(connection == null){
            response.setStatus(202);
            try {
                PrintWriter writer = response.getWriter();
                writer.println("connection is expired! please connect again");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                List<String> databases = infoService.findAllDatabases(connection);
                jsonObject.put("databases",databases);
                JSONObject tablesJson = new JSONObject();
                List<String> tableNames = infoService.findAllTables(connection);
                for(String tableName: tableNames){
                    List<String> columns = infoService.findAllColumns(connection, tableName);
                    tablesJson.put(tableName, columns);
                }
                jsonObject.put("tables", tablesJson);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


    @RequestMapping(value = "/getTables")
    @ResponseBody
    public  JSONObject getTables(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("connection");
        JSONObject jsonObject = new JSONObject();
        if(connection == null){
            response.setStatus(202);
            try {
                PrintWriter writer = response.getWriter();
                writer.println("connection is expired! please connect again");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                List<String> tableNames = infoService.findAllTables(connection);
                for(String tableName: tableNames){
                    List<String> columns = infoService.findAllColumns(connection, tableName);
                    jsonObject.put(tableName, columns);
                }
            } catch (SQLException throwables) {
                response.setStatus(500);
                throwables.printStackTrace();
            }
        }
        return jsonObject;
    }
}
