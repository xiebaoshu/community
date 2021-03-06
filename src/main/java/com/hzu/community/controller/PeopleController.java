package com.hzu.community.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.mapper.*;
import com.hzu.community.service.*;
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
public class PeopleController {

    @Autowired
    private LostArticleService lostArticleService;
    @Autowired
    private HelpArticleService helpArticleService;
    @Autowired
    private SecondArticleService secondArticleService;
    @Autowired
    private JobArticleService jobArticleService;
    @Autowired
    private SchoolArticleService schoolArticleService;
    @Autowired
    private CompanyArticleService companyArticleService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserInfoService userInfoService;
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

        UserInfo people = userInfoService.findUserInfoById(peopleId);
        model.addAttribute("people",people);
//        设置当前页和每页的数据数量
        PageHelper.startPage(page,3);

        //        根据分类id查询文章
        if (parCategory.equals("1")){
            //失物招领模块
            LostArticle lostArticle = new LostArticle();
            lostArticle.setUserInfo(people);
            List<LostArticle> list = new ArrayList<>();
            list=lostArticleService.getArticleList(lostArticle,null);
            PageInfo<LostArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

        }else if (parCategory.equals("2")){
            SecondArticle secondArticle = new SecondArticle();
            secondArticle.setUserInfo(people);
            List<SecondArticle> list = new ArrayList<>();
            list=secondArticleService.getArticleList(secondArticle,null,null,null);
            PageInfo<SecondArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

        }else if (parCategory.equals("3")){
            HelpArticle helpArticle = new HelpArticle();
            helpArticle.setUserInfo(people);
            List<HelpArticle> list = new ArrayList<>();
            list=helpArticleService.getArticleList(helpArticle,null);
            PageInfo<HelpArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);
        }else if (parCategory.equals("4")){
            JobArticle jobArticle = new JobArticle();
            jobArticle.setUserInfo(people);
            List<JobArticle> list = new ArrayList<>();
            list=jobArticleService.getArticleList(jobArticle,null);
            PageInfo<JobArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

        }else if (parCategory.equals("5")){
            SchoolArticle schoolArticle = new SchoolArticle();
            schoolArticle.setUserInfo(people);
            List<SchoolArticle> list = new ArrayList<>();
            list=schoolArticleService.getArticleList(schoolArticle,null);
            PageInfo<SchoolArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

        }else if (parCategory.equals("6")){
            CompanyArticle companyArticle = new CompanyArticle();
            companyArticle.setUserInfo(people);
            List<CompanyArticle> list = new ArrayList<>();
            list=companyArticleService.getArticleList(companyArticle,null);
            PageInfo<CompanyArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

        }else {
            List<Notification> notificationList = new ArrayList<>();
            if (user.getUserId()==peopleId){
//                    我的通知列表
                notificationList = notificationService.getList(peopleId);
            }else {
                notificationList = notificationService.replyList(peopleId);
            }

            PageInfo<Notification> notificationInfo = new PageInfo<>(notificationList);
            model.addAttribute("notificationInfo",notificationInfo);
        }


//        将数据放入model中
        return "admin";
    }

}
