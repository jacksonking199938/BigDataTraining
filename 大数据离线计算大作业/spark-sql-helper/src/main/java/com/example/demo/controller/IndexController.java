package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String sayHello(Model model, HttpServletRequest request){
        List<String> databases = new ArrayList<>();
        model.addAttribute("databases", databases);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("columnName",new ArrayList<>());
        jsonObject.put("data",new ArrayList<>());
        model.addAttribute("result", jsonObject);
        return "index";
    }
}