package com.hzu.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.dto.SearchDto;
import com.hzu.community.mapper.*;
import com.hzu.community.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private LostArticleService lostArticleService;
    @Autowired
    private SecondArticleService secondArticleService;
    @Autowired
    private HelpArticleService helpArticleService;
    @Autowired
    private JobArticleService jobArticleService;
    @Autowired
    private SchoolArticleService schoolArticleService;
    @Autowired
    private CompanyArticleService companyArticleService;
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/search")
    public String SearchPage(@RequestParam(name = "search") String search,
                             @RequestParam(name = "type",defaultValue = "all") String type,
                             @RequestParam(name = "page",defaultValue = "1") Integer page,
                             @RequestParam(name = "date", required = false) Integer date,
                             Model model){
        PageHelper.startPage(page,10);
//        将参数回显
        model.addAttribute("searchCondition",search);
        model.addAttribute("typeCondition",type);
        model.addAttribute("dateCondition",date);
        if (type.equals("all")){
//            搜索结果使用插件分页
            SearchDto searchDto = new SearchDto();
            searchDto.setArticleTitle(search);
            searchDto.setDate(date);
            List<SearchDto> searchDtoList = searchService.getAll(searchDto);
            PageInfo<SearchDto> pageInfo = new PageInfo<>(searchDtoList);
            model.addAttribute("pageInfo",pageInfo);
            Integer count = searchService.getCount(searchDto);
//            统计搜索条数
            model.addAttribute("count",count);

        }else if (type.equals("1")){
            LostArticle lostArticle = new LostArticle();
            lostArticle.setArticleTitle(search);
            List<LostArticle> list = lostArticleService.getArticleList(lostArticle,date);
            PageInfo<LostArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = lostArticleService.searchCount(lostArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("2")){
            SecondArticle secondArticle = new SecondArticle();
            secondArticle.setArticleTitle(search);
            List<SecondArticle> list = secondArticleService.getArticleList(secondArticle,date,null,null);
            PageInfo<SecondArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = secondArticleService.searchCount(secondArticle,date,null,null);
            model.addAttribute("count",count);
        }else if (type.equals("3")){
            HelpArticle helpArticle = new HelpArticle();
            helpArticle.setArticleTitle(search);
            List<HelpArticle> list = helpArticleService.getArticleList(helpArticle,date);
            PageInfo<HelpArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = helpArticleService.searchCount(helpArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("4")){
            JobArticle jobArticle = new JobArticle();
            jobArticle.setArticleTitle(search);
            List<JobArticle> list = jobArticleService.getArticleList(jobArticle,date);
            PageInfo<JobArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = jobArticleService.searchCount(jobArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("5")){
            SchoolArticle schoolArticle = new SchoolArticle();
            schoolArticle.setArticleTitle(search);
            List<SchoolArticle> list = schoolArticleService.getArticleList(schoolArticle,date);
            PageInfo<SchoolArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = schoolArticleService.searchCount(schoolArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("6")){
            CompanyArticle companyArticle = new CompanyArticle();
            companyArticle.setArticleTitle(search);
            List<CompanyArticle> list = companyArticleService.getArticleList(companyArticle,date);
            PageInfo<CompanyArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = companyArticleService.searchCount(companyArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("user")){
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(search);
            List<UserInfo> userInfoList  = userInfoService.searchList(userInfo);
            PageInfo<UserInfo> pageUser = new PageInfo<>(userInfoList);
            model.addAttribute("pageUser",pageUser);

            Integer count = userInfoService.searchCount(userInfo);
            model.addAttribute("count",count);

        }
        return "search";
    }
}
