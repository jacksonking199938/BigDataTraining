package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.serviceImpl.ConnectService;
import com.example.demo.serviceImpl.InfoServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ConnectController {
    @Autowired
    InfoServiceIml infoService;

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connect(HttpServletRequest request, Model model, HttpServletResponse response){
        System.out.println("start to connect");
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> databasesList = new ArrayList<>();
        String url = request.getParameter("url");
        String user = request.getParameter("user");
        String password = request.getParameter(("password"));
        System.out.println(url);
        System.out.println(user);
        System.out.println(password);
        try{
            // 连接spark数据库
//            String url = "jdbc:hive2://bigdata37.depts.bingosoft.net:22237/user29_db";
//            String user = "user29";
//            String password = "pass@bingo29";
            Connection connection = ConnectService.connectToSpark(url,user,password);
            HttpSession session = request.getSession();
            session.setAttribute("connection",connection);
            
            // 下面是获取数据库信息
            List<JSONObject> myTables = new ArrayList<>();
            List<String> tableNames = infoService.findAllTables(connection);
            for(String tableName: tableNames){
                List<String> columns = infoService.findAllColumns(connection, tableName);
                JSONObject temp = new JSONObject();
                temp.put("name",tableName);
                temp.put("columns",columns);
                myTables.add(temp);
            }
            List<String> databaseNames = infoService.findAllDatabases(connection);
            for(String databaseName:databaseNames){
                JSONObject temp = new JSONObject();
                temp.put("name",databaseName);
                if(databaseName.equals(url.substring(url.length()-9))){
                    temp.put("tables",myTables);
                }
                else{
                    temp.put("tables",new ArrayList<>());
                }
                databasesList.add(temp);
            }

        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(500);
            return "index::workArea";
        }
        model.addAttribute("databases", databasesList);
        model.addAttribute("url", url.substring(0,url.length()-9));
        model.addAttribute("currentDB", url.substring(url.length()-9));
        return "index::elementArea";
    }
}
