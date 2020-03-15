package com.hzu.community.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.mapper.LostArticleMapper;
import com.hzu.community.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller

public class AdminController {
    @Autowired
    ArticleCategoryService articleCategoryService;
    @Autowired
    LostArticleMapper lostArticleMapper;
//    个人页面失物招领模块初始化
    @GetMapping("/admin/{parCategory}")
    public String adminInit(@PathVariable("parCategory") String  parCategory,
                            Model model,HttpServletRequest request,
                            @RequestParam(name = "page", defaultValue = "1") Integer page

                            ){
//        获取session中的用户数据
        UserInfo user = new UserInfo();
        user.setUserId(1);
        user.setUserName("谢豪");
        request.getSession().setAttribute("user", user);
        UserInfo owner = (UserInfo) request.getSession().getAttribute("user");
//        设置当前页和每页的数据数量
        PageHelper.startPage(page,3);
//        根据分类id查询文章
        switch(parCategory){
            case "lost" :
                //失物招领模块

                LostArticle lostArticle = new LostArticle();
                lostArticle.setUserInfo(owner);
                List<LostArticle> list = new ArrayList<>();
                list=lostArticleMapper.getArticleList(lostArticle,null);
                PageInfo<LostArticle> pageInfo = new PageInfo<>(list);
                model.addAttribute("pageInfo",pageInfo);
                break; //可选
            case "second" :
                //二手交易模块

                break; //可选
            case "help" :
                //语句
                break; //可选
            case "job" :
                //语句
                break; //可选
            case "university" :
                //语句
                break; //可选
            case "business" :
                //语句
                break; //可选


        }
//        将数据放入model中
        return "admin";
    }
}
