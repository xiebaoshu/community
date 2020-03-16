package com.hzu.community.controller;

import com.hzu.community.bean.UserInfo;
import com.hzu.community.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class signController {
    @Autowired
    UserInfoMapper userInfoMapper;
    @GetMapping("/")
    public String signPage(){

        return "sign";
    }
    @PostMapping(value = "/")
    public String sign(@RequestParam(name = "username") String username,
                       @RequestParam(name = "password") String password,
                       HttpServletResponse response,
                       Model model)
    {
        List<UserInfo> list = userInfoMapper.sign(username,password);
        if (list.size()>0){

            String userId =  list.get(0).getUserId().toString();
            System.out.println(userId);
            Cookie cookie = new Cookie("userId",userId);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            return "redirect:/lost";
        }else{
            model.addAttribute("msg","账号密码错误");
            return "sign";
        }
    }
}