package com.hzu.community.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.SearchDto;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.service.ModifyUserService;
import com.hzu.community.service.UserInfoService;
import com.hzu.community.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ModifyUserController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ModifyUserService modifyUserService;


    @GetMapping("/user")
    public String userPage(@ModelAttribute("user") UserInfo user,
                           @RequestParam(name = "page",defaultValue = "1") Integer page,
                           Model model){

        PageHelper.startPage(page,10);
        List<UserInfo> userList = userInfoService.searchList(user);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userList);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("user",user);
        return "admin/user";
    }
    @GetMapping("/user/ban")
    public String ban(@RequestParam(name = "Id") Integer Id,RedirectAttributes attributes){
        UserInfo user = new UserInfo();
        user.setUserId(Id);
        user.setPermission(true);
        int num = userInfoService.updatePermission(user);
        if (num>0){
            attributes.addFlashAttribute("message", "禁言成功");
        }else {
            attributes.addFlashAttribute("message", "禁言失败");
        }
        return "redirect:/admin/user";

    }
    @GetMapping("/user/unban")
    public String unban(@RequestParam(name = "Id") Integer Id,RedirectAttributes attributes){
        UserInfo user = new UserInfo();
        user.setUserId(Id);
        user.setPermission(false);
        int num = userInfoService.updatePermission(user);
        if (num>0){
            attributes.addFlashAttribute("message", "取消成功");
        }else {
            attributes.addFlashAttribute("message", "取消失败");
        }
        return "redirect:/admin/user";
    }
    @GetMapping("/user/delAll")
    public String delAll(@RequestParam(name = "Id") Integer Id,RedirectAttributes attributes){
        try {
            Boolean b = modifyUserService.delAll(Id);
            if (b.equals(true)){
             attributes.addFlashAttribute("message", "删除成功");
            }
        }catch (Exception e){
            attributes.addFlashAttribute("message", "删除失败"+e.getMessage());
        }
        return "redirect:/admin/user";

    }
}

