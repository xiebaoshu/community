package com.hzu.community.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.Notification;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.mapper.LostArticleMapper;
import com.hzu.community.mapper.UserInfoMapper;
import com.hzu.community.service.ArticleCategoryService;
import com.hzu.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/people")
public class AdminController {
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private LostArticleMapper lostArticleMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserInfoMapper userInfoMapper;
//    个人页面失物招领模块初始化
    @GetMapping("/{peopleId}/{parCategory}")
    public String adminInit(@PathVariable("parCategory") String  parCategory,
                            @PathVariable("peopleId") Integer  peopleId,
                            Model model,HttpServletRequest request,
                            @RequestParam(name = "page", defaultValue = "1") Integer page

                            ){
//        获取session中的用户数据

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
//         判断该个人页面是否属于当前用户
        if (user.getUserId() == peopleId){
            model.addAttribute("owner",true);
        }else{
            model.addAttribute("owner",false);
        }

        UserInfo people = userInfoMapper.findUserInfoById(peopleId);
        model.addAttribute("people",people);
//        设置当前页和每页的数据数量
        PageHelper.startPage(page,3);
//        根据分类id查询文章
        switch(parCategory){
            case "1" :
                //失物招领模块

                LostArticle lostArticle = new LostArticle();
                lostArticle.setUserInfo(people);
                List<LostArticle> list = new ArrayList<>();
                list=lostArticleMapper.getArticleList(lostArticle,null);
                PageInfo<LostArticle> pageInfo = new PageInfo<>(list);
                model.addAttribute("pageInfo",pageInfo);
                break; //可选
            case "2" :
                //二手交易模块

                break; //可选
            case "3" :
                //语句
                break; //可选
            case "4" :
                //语句
                break; //可选
            case "5" :
                //语句
                break; //可选
            case "6" :
                //语句
                break; //可选
            case "notification" :
                List<Notification> notificationList = new ArrayList<>();
                if (user.getUserId()==peopleId){
//                    我的通知列表
                    notificationList = notificationService.getList(peopleId);
                }else {
                    notificationList = notificationService.replyList(peopleId);
                }

                PageInfo<Notification> notificationInfo = new PageInfo<>(notificationList);
                model.addAttribute("notificationInfo",notificationInfo);
                break; //可选


        }
//        将数据放入model中
        return "admin";
    }
}
