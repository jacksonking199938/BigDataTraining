package com.example.demo.controller;

import com.example.demo.serviceImpl.ConnectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Controller
public class QueryController {
    @RequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response){
        String sql = request.getParameter("sql");
        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("connection");
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
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                System.out.println(resultSet);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }
}
