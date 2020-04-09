package com.hzu.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.dto.SearchDto;
import com.hzu.community.mapper.*;
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
    private SearchMapper searchMapper;
    @Autowired
    private LostArticleMapper lostArticleMapper;
    @Autowired
    private SecondArticleMapper secondArticleMapper;
    @Autowired
    private HelpArticleMapper helpArticleMapper;
    @Autowired
    private JobArticleMapper jobArticleMapper;
    @Autowired
    private SchoolArticleMapper schoolArticleMapper;
    @Autowired
    private CompanyArticleMapper companyArticleMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

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
            List<SearchDto> searchDtoList = searchMapper.getAll(search,date);
            PageInfo<SearchDto> pageInfo = new PageInfo<>(searchDtoList);
            model.addAttribute("pageInfo",pageInfo);
            Integer count = searchMapper.getCount(search,date);
//            统计搜索条数
            model.addAttribute("count",count);

        }else if (type.equals("1")){
            LostArticle lostArticle = new LostArticle();
            lostArticle.setArticleTitle(search);
            List<LostArticle> list = lostArticleMapper.getArticleList(lostArticle,date);
            PageInfo<LostArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = lostArticleMapper.searchCount(lostArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("2")){
            SecondArticle secondArticle = new SecondArticle();
            secondArticle.setArticleTitle(search);
            List<SecondArticle> list = secondArticleMapper.getArticleList(secondArticle,date,null,null);
            PageInfo<SecondArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = secondArticleMapper.searchCount(secondArticle,date,null,null);
            model.addAttribute("count",count);
        }else if (type.equals("3")){
            HelpArticle helpArticle = new HelpArticle();
            helpArticle.setArticleTitle(search);
            List<HelpArticle> list = helpArticleMapper.getArticleList(helpArticle,date);
            PageInfo<HelpArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = helpArticleMapper.searchCount(helpArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("4")){
            JobArticle jobArticle = new JobArticle();
            jobArticle.setArticleTitle(search);
            List<JobArticle> list = jobArticleMapper.getArticleList(jobArticle,date);
            PageInfo<JobArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = jobArticleMapper.searchCount(jobArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("5")){
            SchoolArticle schoolArticle = new SchoolArticle();
            schoolArticle.setArticleTitle(search);
            List<SchoolArticle> list = schoolArticleMapper.getArticleList(schoolArticle,date);
            PageInfo<SchoolArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = schoolArticleMapper.searchCount(schoolArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("6")){
            CompanyArticle companyArticle = new CompanyArticle();
            companyArticle.setArticleTitle(search);
            List<CompanyArticle> list = companyArticleMapper.getArticleList(companyArticle,date);
            PageInfo<CompanyArticle> pageInfo = new PageInfo<>(list);
            model.addAttribute("pageInfo",pageInfo);

            Integer count = companyArticleMapper.searchCount(companyArticle,date);
            model.addAttribute("count",count);
        }else if (type.equals("user")){

            List<UserInfo> userInfoList  = userInfoMapper.searchList(search);
            PageInfo<UserInfo> pageUser = new PageInfo<>(userInfoList);
            model.addAttribute("pageUser",pageUser);

            Integer count = userInfoMapper.searchCount(search);
            model.addAttribute("count",count);

        }
        return "search";
    }
}
