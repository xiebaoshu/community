package com.hzu.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.mapper.UserInfoMapper;
import com.hzu.community.service.NavService;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NavController {
    @Autowired
    private NavService navService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @GetMapping("/")
    public String navPage(Model model,HttpServletRequest request){
//        初始化设置区域 默认列表
        List<Nav> navList = new ArrayList<>();
        navList = navService.navDefault();
        model.addAttribute("navDefault",navList);
        return "nav/nav.html";
    }

//    返回导航区域
    @GetMapping("/nav")
    public String nav(HttpServletRequest request, Model model,
                      @RequestParam(name = "page", defaultValue = "1") Integer page){
//        判断session是否存在user，不存在的话，返回默认提供的导航列表

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        if (user==null){
            PageHelper.startPage(page,8);
            List<Nav> navList = new ArrayList<>();
            navList = navService.navDefault();
            PageInfo<Nav> pageInfo  = new PageInfo<>(navList);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("showType",1);
        }else {
            if (user.getNavType()==1){
                PageHelper.startPage(page,8);
            }else {
                PageHelper.startPage(page,10);
            }
            List<Nav> navList = new ArrayList<>();
            navList = navService.navDiy(user);
            PageInfo<Nav> pageInfo  = new PageInfo<>(navList);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("showType",user.getNavType());

        }

        return "fragments :: nav_area";
    }
//    返回我的添加区域
    @GetMapping("/nav/mySet")
    public String mySet(Model model,HttpServletRequest request){
        //        根据user获取设置区域 我的添加列表
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("hasUser",false);
        }else {
            List<Nav> navDiy = new ArrayList<>();
            navDiy = navService.navDiy(user);
            model.addAttribute("navDiy",navDiy);
            model.addAttribute("hasUser",true);
        }
        return "fragments :: my_set";
    }



    @PostMapping("/nav/add")
    @ResponseBody
    public Map<String,Object> add(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Nav nav = null;
        String navStr = HttpServletRequestUtil.getString(request,"navStr");
        try {
           nav = objectMapper.readValue(navStr,Nav.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        nav.setUserId(user.getUserId());
        int num = navService.add(nav);
        if (num>0){
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","数据库操作失败");
            return modelMap;
        }
        return modelMap;

    }


    @PostMapping("/nav/delete")
    @ResponseBody
    public Map<String,Object> delete(HttpServletRequest request,
                                      @RequestParam(name = "id") Integer id){
        Map<String,Object> modelMap = new HashMap<>();
        try {
            navService.del(id);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;

    }


    @PostMapping("/navShow")
    @ResponseBody
    public Map<String,Object> navShow(HttpServletRequest request,
                                      @RequestParam(name = "showType") Integer showType){
        Map<String,Object> modelMap = new HashMap<>();
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        user.setNavType(showType);
        try {
            userInfoMapper.update(user);
            user = userInfoMapper.findUserInfoById(user.getUserId());
            request.getSession().removeAttribute("user");
            request.getSession().setAttribute("user",user);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;



    }
}
