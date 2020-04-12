package com.hzu.community.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.mapper.ArticleCategoryMapper;
import com.hzu.community.mapper.HelpArticleMapper;
import com.hzu.community.mapper.TagMapper;
import com.hzu.community.service.ArticleCategoryService;
import com.hzu.community.service.HelpArticleService;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/3")
public class HelpArticleController {
   @Autowired
   private TagMapper tagMapper;
   @Autowired
   private ArticleCategoryService articleCategoryService;
   @Autowired
   private HelpArticleService helpArticleService;
   @Autowired
   private HelpArticleMapper helpArticleMapper;

    @GetMapping("")
    public String ArticlePage(Model model,
                                  HttpServletRequest request,
                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "search", required = false) String search,
                                  @RequestParam(name = "category", required = false) Integer category,
                                  @RequestParam(name = "date", required = false) Integer date,
                                  @RequestParam(name = "tagPar", defaultValue = "1") Integer tagPar,
                                  @RequestParam(name = "tag", required = false) String tag
    ){

//        取出子类别和标签组
        List<ArticleCategory> articleCategories = articleCategoryService.getArticleCategories(3);
        List<Tag> tagList = tagMapper.allTag(3);
        model.addAttribute("articleCategories",articleCategories);
        model.addAttribute("tagList",tagList);
//        返回查询条件，使元素回显
        model.addAttribute("dateCondition",date);
        model.addAttribute("categoryCondition",category);
        model.addAttribute("searchCondition",search);
        model.addAttribute("tagCondition",tag);
        model.addAttribute("tagParCondition",tagPar);
        //判断属于哪种文章，高亮
        model.addAttribute("articleParCategory",3);

        // 从session获取用户信息，并返回给前台显示在页面上

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        model.addAttribute("user",user);

//        封装查询条件，并作为参数查询
        HelpArticle article = new HelpArticle();

        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleCategoryId(category);
        article.setArticleCategory(articleCategory);
        article.setArticleTitle(search);
        article.setTag(tag);
        //开启分页，并使用pageHelp插件进行分页和返回数据，pageHelp插件需要先配置pom和yml。
        PageHelper.startPage(page,10);
        List<HelpArticle> list = new ArrayList<>();
        list=helpArticleMapper.getArticleList(article,date);
        PageInfo<HelpArticle> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "/help/help";
    }



    @GetMapping("/add")
    public String toAddPage(Model model){
        //    获取校内互助新增页面初始化信息
        List<ArticleCategory> articleCategoryList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();
        try {
            articleCategoryList =  articleCategoryService.getArticleCategories(3);
            tagList = tagMapper.allTag(3);
            model.addAttribute("categoryList",articleCategoryList);
            model.addAttribute("tagList",tagList);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

//        返回视图
        return  "/help/help-input";
    }

    //    校内互助新增页面数据处理,并加入数据库
    @RequestMapping(value="/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> add(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();


        ObjectMapper mapper = new ObjectMapper();
        HelpArticle article = null;
        String articleStr = HttpServletRequestUtil.getString(request, "articleStr");

        try {

            article = mapper.readValue(articleStr, HelpArticle.class);

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
                le = helpArticleService.saveArticle(article,imageHolder);

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
                               @RequestParam(name = "articleId") Integer articleId,
                               HttpServletRequest request){
        //通过articleId回显article里面的数据
        HelpArticle article = helpArticleMapper.findArticleById(articleId);
        model.addAttribute("article",article);

        //        判断是否为非法操作
        UserInfo user = article.getUserInfo();
        UserInfo nowUser = (UserInfo)request.getSession().getAttribute("user");
//        如果是非法操作，则重定向到当前用户界面
        if (!user.getUserId().equals(nowUser.getUserId()) && !nowUser.getUserType().equals(3)){
            return "redirect:/people/"+nowUser.getUserId()+"/3";
        }
        //        页面初始化所需数据
        List<ArticleCategory> categoryList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();
        categoryList = articleCategoryService.getArticleCategories(3);
        tagList = tagMapper.allTag(3);
//       将需要下拉列表选项的数据放进去model
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("tagList",tagList);

        return "/help/help-input";
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String,Object> update(HttpServletRequest request){
        Map<String,Object>modelMap = new HashMap<String,Object>();
        ObjectMapper mapper = new ObjectMapper();
        HelpArticle article = null;
        String articleStr = HttpServletRequestUtil.getString(request, "articleStr");
        try {
            article = mapper.readValue(articleStr, HelpArticle.class);
            //将页面提交的helpArticle信息传入
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

            ArticleExecution le;
            try {
                if (articleImg == null){
                    le=helpArticleService.updateArticle(article,null);
                }else {
                    //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
                    //调用service添加帖子信息和图片信息
                    le = helpArticleService.updateArticle(article,imageHolder);
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
                                    RedirectAttributes attributes,
                                  HttpServletRequest request){

        HelpArticle article = helpArticleMapper.findArticleById(articleId);
//        文章作者
        UserInfo user = article.getUserInfo();
//        当前用户
        UserInfo nowUser = (UserInfo)request.getSession().getAttribute("user");
        // userId用于删除本地存储图片和所在文件夹
        Integer userId = user.getUserId();
        //如果当前用户与文章作者相等，则为合法操作
        if (user.getUserId().equals(nowUser.getUserId())){
            try {
                helpArticleService.deleteArticle(articleId,userId);
            }catch (ArticleException e){
                System.out.println(e.getMessage());
            }
            return "redirect:/people/"+userId+"/3";
        }else if (nowUser.getUserType().equals(3)){
//          如果是管理员权限，也可进行操作
            try {
                helpArticleService.deleteArticle(articleId,userId);
            }catch (ArticleException e){
                attributes.addFlashAttribute("message", "删除失败"+e.getMessage());
                System.out.println(e.getMessage());
            }
            attributes.addFlashAttribute("message", "删除成功");
            return "redirect:/admin/article";
        }else {
//            非法操作，不进行操作，返回用户界面
            return "redirect:/people/"+nowUser.getUserId()+"/3";
        }


    }

    @GetMapping("/{articleId}")
    public String articleDetail(@PathVariable("articleId") Integer articleId,Model model
    ){
        HelpArticle article = helpArticleMapper.findArticleById(articleId);
        model.addAttribute("article",article);
        helpArticleMapper.incReadCount(article);
        return "/help/article-detail";
    }
}
