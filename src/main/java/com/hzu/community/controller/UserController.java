package com.hzu.community.controller;

import com.hzu.community.bean.HelpArticle;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.enums.UserEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.mapper.UserInfoMapper;
import com.hzu.community.service.UserInfoService;
import org.apache.catalina.Session;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {
    static Boolean hasUser;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserInfoService userInfoService;
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
            UserInfo user = (UserInfo)request.getSession().getAttribute("user");
            if (user.getUserType().equals(3)){
                return "redirect:/admin/article";
            }else {
                return "redirect:/1";
            }

        }else{
            return "login/login";
        }


    }
    @PostMapping(value = "/login")
    public String sign(@ModelAttribute("user") UserInfo user,
                       HttpServletResponse response,
                       Model model)
    {
        List<UserInfo> list = userInfoMapper.login(user);
        if (list.size()>0){
            user =  list.get(0);
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userInfoMapper.update(user);
            Cookie cookie = new Cookie("token",token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            if (user.getUserType().equals(3)){
                return "redirect:/admin/article";
            }else {
                return "redirect:/1";
            }

        }else{
            model.addAttribute("message","账号密码错误");
            return "login/login";
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

    @GetMapping("/register")
    public String register() {
       return "login/register";
    }
    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user") UserInfo user,
                               RedirectAttributes attributes,
                               HttpServletRequest request) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserAccount(user.getUserAccount());
        List<UserInfo> hasUser = userInfoMapper.login(userInfo);
        if (hasUser.size()>0){
            attributes.addFlashAttribute("message", "注册失败，该账号已存在");
        }else {
            UserEnum le;
            MultipartFile UserImg = user.getUpload();
            try {
                if (UserImg.isEmpty()){
                    le = userInfoService.save(user,null);
                }else {
                    //获取文件名和文件流，并作为封装到自定义类imageHolder里面。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(UserImg.getOriginalFilename(),UserImg.getInputStream());
//               调用service添加帖子信息和图片信息
                    le = userInfoService.save(user,imageHolder);
                }

                if(le.getState() == ArticleEnum.SUCCESS.getState()){
                    attributes.addFlashAttribute("message", "注册成功");

                }else{
                    attributes.addFlashAttribute("message", "注册失败");
                }
            }catch(ArticleException e){
//               处理service层抛出的异常
                attributes.addFlashAttribute("message", "注册失败"+e.getMessage());
            }catch(IOException e){
                attributes.addFlashAttribute("message", "注册失败"+e.getMessage());
            }
        }

        return "redirect:/login";

    }

    @GetMapping("/update")
    public String update(Model model,
                           @RequestParam(name = "userId") Integer userId,
                           HttpServletRequest request) {
        UserInfo user = (UserInfo)request.getSession().getAttribute("user");
        if (!user.getUserId().equals(userId)){
//            非法操作
            return "redirect:/people/"+user.getUserId()+"/1";
        }else {
            UserInfo userInfo = userInfoMapper.findUserInfoById(userId);
            model.addAttribute("user",userInfo);
            return "login/register";
        }

    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("user") UserInfo user,
                             HttpServletRequest request,
                             RedirectAttributes attributes) {

        MultipartFile UserImg = user.getUpload();

        if (user.getUserId()!= null) {

            UserEnum le;
            try {
                if (UserImg.isEmpty()){
                    le=userInfoService.update(user,null);
                }else {
                    //获取文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(UserImg.getOriginalFilename(),UserImg.getInputStream());
                    //调用service添加帖子信息和图片信息
                    le = userInfoService.update(user,imageHolder);
                }


                if(le.getState() == ArticleEnum.SUCCESS.getState()){
                    attributes.addFlashAttribute("message", "修改成功");

                }else{
                    attributes.addFlashAttribute("message", "修改失败");
                }
            }catch(ArticleException e){
//               处理service层抛出的异常
                attributes.addFlashAttribute("message", "修改失败"+e.getMessage());
            }catch(IOException e){
                attributes.addFlashAttribute("message", "修改失败"+e.getMessage());
            }
            return "redirect:/people/"+user.getUserId()+"/1";

        }else{
            attributes.addFlashAttribute("message", "修改失败,页面未返回id");
            return "redirect:/people/"+user.getUserId()+"/1";

        }

    }

}
