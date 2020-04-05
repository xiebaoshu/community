package com.hzu.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.mapper.CompanyArticleMapper;
import com.hzu.community.mapper.TagMapper;
import com.hzu.community.service.ArticleCategoryService;
import com.hzu.community.service.CompanyArticleService;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

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
    private TagMapper tagMapper;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private CompanyArticleService companyArticleService;
    @Autowired
    private CompanyArticleMapper companyArticleMapper;

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
        List<Tag> tagList = tagMapper.allTag(6);
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
        list=companyArticleMapper.getArticleList(article,date);
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
            tagList = tagMapper.allTag(6);
            model.addAttribute("categoryList",articleCategoryList);
            model.addAttribute("tagList",tagList);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

//        返回视图
        return  "/company/company-input";
    }

    //    企业信息新增页面数据处理,并加入数据库
    @RequestMapping(value="/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> add(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();


        ObjectMapper mapper = new ObjectMapper();
        CompanyArticle article = null;
        String articleStr = HttpServletRequestUtil.getString(request, "articleStr");

        try {

            article = mapper.readValue(articleStr, CompanyArticle.class);

            //将页面提交的article信息传入
        } catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }


        CommonsMultipartFile articleImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //获取上传的图片文件，需要pom.xml配置commons-fileupload，需在springweb配置文件上传解析器multipartResolver
        if(commonsMultipartResolver.isMultipart(request)){

            MultipartHttpServletRequest MultipartHttpServletRequest = (MultipartHttpServletRequest) request;
            articleImg = (CommonsMultipartFile) MultipartHttpServletRequest.getFile("articleImg");
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","无法获取图片文件流");
            return modelMap;
        }

//        新增校园互助
        if (article != null && articleImg != null){
//            从session获取数据
            UserInfo owner = (UserInfo) request.getSession().getAttribute("user");
            article.setUserInfo(owner);
            ArticleExecution le;
            try {
                //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
//               调用service添加帖子信息和图片信息
                le = companyArticleService.saveArticle(article,imageHolder);

                if(le.getState() == ArticleEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                    modelMap.put("user",owner);

                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg","添加失败，请检查数据");
                    return modelMap;
                }
            }catch(ArticleException e){
//               处理service层抛出的异常
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }catch(IOException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
            return modelMap;


        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","信息不全");
            return modelMap;

        }

    }

    @RequestMapping("/update")
    public String toUpdatePage(Model model,
                               @RequestParam(name = "articleId") Integer articleId){

        List<ArticleCategory> categoryList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();
        categoryList = articleCategoryService.getArticleCategories(6);
        tagList = tagMapper.allTag(6);
//       将需要下拉列表选项的数据放进去model
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("tagList",tagList);
        //通过articleId回显article里面的数据
        CompanyArticle article = companyArticleMapper.findArticleById(articleId);
        model.addAttribute("article",article);
        return "/company/company-input";
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String,Object> update(HttpServletRequest request){
        Map<String,Object>modelMap = new HashMap<String,Object>();
        ObjectMapper mapper = new ObjectMapper();
        CompanyArticle article = null;
        String articleStr = HttpServletRequestUtil.getString(request, "articleStr");
        try {
            article = mapper.readValue(articleStr, CompanyArticle.class);
            //将页面提交的companyArticle信息传入
        } catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile articleImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //获取上传的图片文件，需要pom.xml配置commons-fileupload，需在springweb配置文件上传解析器multipartResolver
        if(commonsMultipartResolver.isMultipart(request)){

            MultipartHttpServletRequest MultipartHttpServletRequest = (MultipartHttpServletRequest) request;
            articleImg = (CommonsMultipartFile) MultipartHttpServletRequest.getFile("articleImg");
            //        更新操作中，图片不是必须，所以不做else处理
        }

        if (article != null && article.getId()!= null) {
//            从session获取数据
            UserInfo owner = (UserInfo) request.getSession().getAttribute("user");
            article.setUserInfo(owner);
            ArticleExecution le;
            try {
                if (articleImg == null){
                    le=companyArticleService.updateArticle(article,null);
                }else {
                    //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
                    //调用service添加帖子信息和图片信息
                    le = companyArticleService.updateArticle(article,imageHolder);
                }


                if(le.getState() == ArticleEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                    modelMap.put("user",owner);

                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",le.getStateInfo());
                    return modelMap;
                }
            }catch(ArticleException e){
//               处理service层抛出的异常
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }catch(IOException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
            return modelMap;

        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","页面未返回文章id");
            return modelMap;

        }

    }

    @PostMapping("/delete")
    public String del(@RequestParam(name = "articleId") Integer articleId,
                      HttpServletRequest request){
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        Integer userId = user.getUserId();
//        userId用于删除本地存储图片和所在文件夹
        try {
            companyArticleService.deleteArticle(articleId,userId);
        }catch (ArticleException e){
            System.out.println(e.getMessage());
        }
        return "redirect:/people/"+userId+"/6";

    }

    @GetMapping("/{articleId}")
    public String articleDetail(@PathVariable("articleId") Integer articleId,Model model
    ){
        CompanyArticle article = companyArticleMapper.findArticleById(articleId);
        model.addAttribute("article",article);
        companyArticleMapper.incReadCount(article);
        return "/company/article-detail";
    }
}
