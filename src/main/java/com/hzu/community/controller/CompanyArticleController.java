package com.hzu.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;

import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.mapper.CompanyArticleMapper;
import com.hzu.community.mapper.TagMapper;
import com.hzu.community.service.ArticleCategoryService;
import com.hzu.community.service.CompanyArticleService;
import com.hzu.community.service.TagService;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/6")
public class CompanyArticleController {
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private CompanyArticleService companyArticleService;


    @GetMapping("")
    public String ArticlePage(Model model,
                              HttpServletRequest request,
                              @RequestParam(name = "page", defaultValue = "1") Integer page,
                              @RequestParam(name = "search", required = false) String search,
                              @RequestParam(name = "category", required = false) Integer category,
                              @RequestParam(name = "date", required = false) Integer date,
                              @RequestParam(name = "tagPar", defaultValue = "44") Integer tagPar,
                              @RequestParam(name = "tag", required = false) String tag
    ){

//        取出子类别和标签组
        List<ArticleCategory> articleCategories = articleCategoryService.getArticleCategories(6);
        List<Tag> tagList = tagService.allTag(6);
        model.addAttribute("articleCategories",articleCategories);
        model.addAttribute("tagList",tagList);
//        返回查询条件，使元素回显
        model.addAttribute("dateCondition",date);
        model.addAttribute("categoryCondition",category);
        model.addAttribute("searchCondition",search);
        model.addAttribute("tagCondition",tag);
        model.addAttribute("tagParCondition",tagPar);
        //判断属于哪种文章，高亮
        model.addAttribute("articleParCategory",6);

        // 从session获取用户信息，并返回给前台显示在页面上

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        model.addAttribute("user",user);

//        封装查询条件，并作为参数查询
        CompanyArticle article = new CompanyArticle();

        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleCategoryId(category);
        article.setArticleCategory(articleCategory);
        article.setArticleTitle(search);
        article.setTag(tag);
        //开启分页，并使用pageCompany插件进行分页和返回数据，pageCompany插件需要先配置pom和yml。
        PageHelper.startPage(page,10);
        List<CompanyArticle> list = new ArrayList<>();
        list=companyArticleService.getArticleList(article,date);
        PageInfo<CompanyArticle> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "/company/company";
    }
    
    @GetMapping("/add")
    public String toAddPage(Model model){
        //    获取企业信息新增页面初始化信息
        List<ArticleCategory> articleCategoryList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();
        try {
            articleCategoryList =  articleCategoryService.getArticleCategories(6);
            tagList = tagService.allTag(6);
            model.addAttribute("categoryList",articleCategoryList);
            model.addAttribute("tagList",tagList);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

//        返回视图
        return  "/company/company-input";
    }

    //    企业信息新增页面数据处理,并加入数据库
    @PostMapping("/add")
    public String add(@ModelAttribute("article") CompanyArticle article,
                      RedirectAttributes attributes,
                      HttpServletRequest request){
//        从session获取用户信息
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        // 根据用户类型，判断返回位置
        String redirectUrl = new String();
        if (user.getUserType()==3){
            redirectUrl = "redirect:/admin/article";
        }else {
            redirectUrl = "redirect:/people/"+user.getUserId()+"/6";
        }

        MultipartFile articleImg = article.getUpload();
//        新增校园互助
            article.setUserInfo(user);
            ArticleEnum le;
            try {
                if (articleImg.isEmpty()){
                    le = companyArticleService.saveArticle(article,null);
                }else {
                    //获取文件名和文件流，并作为封装到自定义类imageHolder里面。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
//               调用service添加帖子信息和图片信息
                    le = companyArticleService.saveArticle(article,imageHolder);
                }


                if(le.getState() == ArticleEnum.SUCCESS.getState()){
                    attributes.addFlashAttribute("message", "新增成功");
                }else{
                    attributes.addFlashAttribute("message", "新增失败");

                }
            }catch(ArticleException e){
//               处理service层抛出的异常
                attributes.addFlashAttribute("message", "新增失败"+e.getMessage());

            }catch(IOException e){
                attributes.addFlashAttribute("message", "新增失败"+e.getMessage());

            }


                return redirectUrl;






    }

    @RequestMapping("/update")
    public String toUpdatePage(Model model,
                               @RequestParam(name = "articleId") Integer articleId,
                               HttpServletRequest request){
        //通过articleId回显article里面的数据
        CompanyArticle article = companyArticleService.findArticleById(articleId);
        model.addAttribute("article",article);
        //        判断是否为非法操作
        UserInfo user = article.getUserInfo();
        UserInfo nowUser = (UserInfo)request.getSession().getAttribute("user");
//        如果是非法操作，则抛出异常，跳转到错误页面
        if (!user.getUserId().equals(nowUser.getUserId()) && !nowUser.getUserType().equals(3)){
            throw new ArticleException("非法操作，请不要修改他人文章");
        }
        //        页面初始化所需数据
        List<ArticleCategory> categoryList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();
        categoryList = articleCategoryService.getArticleCategories(6);
        tagList = tagService.allTag(6);
//       将需要下拉列表选项的数据放进去model
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("tagList",tagList);

        return "/company/company-input";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("article") CompanyArticle article,
                                     HttpServletRequest request,
                                    RedirectAttributes attributes){

        //从session获取用户信息
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        // 根据用户类型，判断返回位置
        String redirectUrl = new String();
        if (user.getUserType()==3){
            redirectUrl = "redirect:/admin/article";
        }else {
            redirectUrl = "redirect:/people/"+user.getUserId()+"/6";
        }

        MultipartFile articleImg = article.getUpload();

        if (article.getId()!= null) {

            ArticleEnum le;
            try {
                if (articleImg.isEmpty()){
                    le=companyArticleService.updateArticle(article,null);
                }else {
                    //获取文件名和文件流，并作为封装到自定义类imageHolder里面。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
                    //调用service添加帖子信息和图片信息
                    le = companyArticleService.updateArticle(article,imageHolder);
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
            return redirectUrl;

        }else{
            attributes.addFlashAttribute("message", "修改失败,页面未返回id");
            return redirectUrl;
        }

    }

    @PostMapping("/delete")
    public String del(@RequestParam(name = "articleId") Integer articleId,
                      RedirectAttributes attributes,
                      HttpServletRequest request){

        CompanyArticle article = companyArticleService.findArticleById(articleId);
//        文章作者
        UserInfo user = article.getUserInfo();
//        当前用户
        UserInfo nowUser = (UserInfo)request.getSession().getAttribute("user");
        // userId用于删除本地存储图片和所在文件夹
        Integer userId = user.getUserId();
        //如果当前用户与文章作者相等，则为合法操作
        if (user.getUserId().equals(nowUser.getUserId())){
            try {
                companyArticleService.deleteArticle(articleId,userId);
            }catch (ArticleException e){
                System.out.println(e.getMessage());
            }
            attributes.addFlashAttribute("message", "删除成功");
            return "redirect:/people/"+userId+"/6";
        }else if (nowUser.getUserType().equals(3)){
//          如果是管理员权限，也可进行操作
            try {
                companyArticleService.deleteArticle(articleId,userId);
            }catch (ArticleException e){
                attributes.addFlashAttribute("message", "删除失败"+e.getMessage());
                System.out.println(e.getMessage());
            }
            attributes.addFlashAttribute("message", "删除成功");
            return "redirect:/admin/article";
        }else {
//            非法操作，抛出异常
            throw new ArticleException("非法操作，请不要删除他人文章");
        }


    }

    @GetMapping("/{articleId}")
    public String articleDetail(@PathVariable("articleId") Integer articleId,Model model
    ){
        CompanyArticle article = companyArticleService.findArticleById(articleId);
        model.addAttribute("article",article);
        companyArticleService.incReadCount(article);
        return "/company/article-detail";
    }
}
