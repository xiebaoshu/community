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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
public class signController {
    static Boolean hasUser;
    @Autowired
    UserInfoMapper userInfoMapper;
    @GetMapping("/login")
    public String signPage(HttpServletRequest request){
        hasUser =false;
//       通过cookies判断用户是否登陆
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                     hasUser = true;
                    break;
                }
            }
        }
        if(hasUser){
            // 如果已登陆跳转到展示页
            return "redirect:/1";
        }else{
            return "sign";
        }


    }
    @PostMapping(value = "/login")
    public String sign(@RequestParam(name = "username") String username,
                       @RequestParam(name = "password") String password,
                       HttpServletResponse response,
                       Model model)
    {
        List<UserInfo> list = userInfoMapper.sign(username,password);
        if (list.size()>0){

            UserInfo user =  list.get(0);
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userInfoMapper.update(user);

            Cookie cookie = new Cookie("token",token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            return "redirect:/1";
        }else{
            model.addAttribute("msg","账号密码错误");
            return "sign";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
