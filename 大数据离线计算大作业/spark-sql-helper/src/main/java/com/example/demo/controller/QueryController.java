package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QueryController {
    @RequestMapping("/query")
    public String query(HttpServletRequest request, Model model, HttpServletResponse response){
        System.out.println("enter query");
        String sql = request.getParameter("sql");
        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("connection");
        if(connection == null){
            response.setStatus(202);
        }
        else{
            try {
                Statement statement = connection.createStatement();
                String [] sqls = sql.split(";");
                ResultSet resultSet = null;
                for(String s:sqls) {
                    s.replaceAll("\t|\r|\n"," ");
                    System.out.println(s);
                    resultSet = statement.executeQuery(s);
                    System.out.println(resultSet);
                }
                if(resultSet != null) {
                    ResultSetMetaData metadata = resultSet.getMetaData();
                    int columnCount = metadata.getColumnCount();
                    List<String> columnNames = new ArrayList<>(columnCount);
                    List<List<String>> data = new ArrayList<>(resultSet.getFetchSize());
                    for(int i=1; i<= columnCount;i++){
                        columnNames.add(metadata.getColumnName(i));
                    }
                    while(resultSet.next()){
                        List<String> temp = new ArrayList<>(columnCount);
                        for(int i=1; i<= columnCount;i++){
                            temp.add(resultSet.getString(i));
                        }
                        data.add(temp);
                    }
                    model.addAttribute("columnNames",columnNames);
                    model.addAttribute("data",data);
                }

            } catch (Exception throwables) {
                response.setStatus(500);
                throwables.printStackTrace();
            }
        }
        return "index::result";
    }
}
