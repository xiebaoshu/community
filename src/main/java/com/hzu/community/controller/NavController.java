package com.hzu.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.service.NavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class NavController {
    @Autowired
    private NavService navService;
    @GetMapping("/")
    public String navPage(){
        return "nav/nav.html";
    }
    @GetMapping("/nav")
    public String nav(HttpServletRequest request, Model model,
                      @RequestParam(name = "page", defaultValue = "1") Integer page){
//        判断session是否存在user，存在的话，返回默认提供的导航列表

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        PageHelper.startPage(page,8);
        List<Nav> navList = new ArrayList<>();
        navList = navService.navDefault();

        PageInfo<Nav> pageInfo  = new PageInfo<>(navList);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("user",user);

        return "fragments :: nav_area";
    }
}
