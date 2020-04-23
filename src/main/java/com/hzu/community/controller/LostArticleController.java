package com.hzu.community.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.mapper.CommentMapper;
import com.hzu.community.mapper.LostArticleMapper;
import com.hzu.community.mapper.TagMapper;
import com.hzu.community.mapper.UserInfoMapper;
import com.hzu.community.service.*;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/1")
public class LostArticleController {
    @Autowired
    AreaService areaService;
    @Autowired
    ItemCategoryService itemCategoryService;
    @Autowired
    ArticleCategoryService articleCategoryService;
    @Autowired
    LostArticleService lostArticleService;
    @Autowired
    LostArticleMapper lostArticleMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentService commentService;
    @Autowired
    private TagMapper tagMapper;

    @GetMapping("")
    public String lostArticleList(Model model,
                                  HttpServletRequest request,
                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "search", required = false) String search,
                                  @RequestParam(name = "item", required = false) Integer item,
                                  @RequestParam(name = "area", required = false) Integer area,
                                  @RequestParam(name = "category", required = false) Integer category,
                                  @RequestParam(name = "tag", required = false) String tag,
                                  @RequestParam(name = "tagPar", defaultValue = "56") Integer tagPar,
                                  @RequestParam(name = "date", required = false) Integer date

    ){

//        初始化信息
        List<Area> areaList = areaService.getAreaList();
        model.addAttribute("areaList",areaList);
        List<ItemCategory> itemCategoryList = itemCategoryService.getItemCategoryist();
        model.addAttribute("itemCategoryList",itemCategoryList);
        List<Tag> tagList = tagMapper.allTag(1);
        model.addAttribute("tagList",tagList);
//        取出失物招领子类别
        List<ArticleCategory> articleCategories = articleCategoryService.getArticleCategories(1);
        model.addAttribute("articleCategories",articleCategories);
//        返回查询条件，使页面下拉菜单选中
        model.addAttribute("areaCondition",area);
        model.addAttribute("itemCondition",item);
        model.addAttribute("dateCondition",date);
        model.addAttribute("categoryCondition",category);
        model.addAttribute("searchCondition",search);
        model.addAttribute("tagCondition",tag);
        model.addAttribute("tagParCondition",tagPar);
        //判断属于哪种文章，高亮
        model.addAttribute("articleParCategory",1);

        // 从session获取用户信息，并返回给前台显示在页面上

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        model.addAttribute("user",user);

//        封装查询条件，并作为参数查询
        LostArticle lostArticle = new LostArticle();

        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleCategoryId(category);
        lostArticle.setArticleCategory(articleCategory);

        Area area1 = new Area();
        area1.setAreaId(area);
        lostArticle.setArea(area1);

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryId(item);
        lostArticle.setItemCategory(itemCategory);
        lostArticle.setTag(tag);
        lostArticle.setArticleTitle(search);
        //开启分页，并使用pageHelp插件进行分页和返回数据，pageHelp插件需要先配置pom和yml。
        PageHelper.startPage(page,10);
        List<LostArticle> list = new ArrayList<>();
        list=lostArticleMapper.getArticleList(lostArticle,date);

        PageInfo<LostArticle> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "/lost/lost";
    }



    //    获取失物招领新增页面初始化信息
    @GetMapping("/add")
    public String toAddPage(Model model){
        List<Tag> tagList = new ArrayList<>();
        List<Area> areaList = new ArrayList<>();
        List<ItemCategory> itemCategoryList = new ArrayList<>();
        List<ArticleCategory> articleCategoryList = new ArrayList<>();
        try {
            areaList = areaService.getAreaList();
            itemCategoryList = itemCategoryService.getItemCategoryist();
            articleCategoryList = articleCategoryService.getArticleCategories(1);
            tagList = tagMapper.allTag(1);
//            将需要初始化的数据放进去model
            model.addAttribute("areaList",areaList);
            model.addAttribute("itemCategoryList",itemCategoryList);
            model.addAttribute("tagList",tagList);
            model.addAttribute("articleCategoryList",articleCategoryList);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
//        返回视图
        return  "/lost/lost-input";
    }


//    失物招领新增页面数据处理,并加入数据库
    @PostMapping("/add")
    public String add(@ModelAttribute("article") LostArticle article,
                      RedirectAttributes attributes,
                      HttpServletRequest request){
        // 从session获取用户信息
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        // 根据用户类型，判断返回位置
        String redirectUrl = new String();
        if (user.getUserType()==3){
            redirectUrl = "redirect:/admin/article";
        }else {
            redirectUrl = "redirect:/people/"+user.getUserId()+"/1";
        }

        MultipartFile articleImg = article.getUpload();

//        新增失物招领


            article.setUserInfo(user);
            ArticleEnum le;
            try {
                if (articleImg.isEmpty()){
                    le = lostArticleService.saveArticle(article,null);
                }else {
                    //将获取文件名和文件流，并作为封装到自定义类imageHolder里面。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
//               调用service添加帖子信息和图片信息
                    le = lostArticleService.saveArticle(article,imageHolder);
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
        LostArticle article = lostArticleMapper.findArticleById(articleId);
        model.addAttribute("article",article);
//        判断是否为非法操作
        UserInfo user = article.getUserInfo();
        UserInfo nowUser = (UserInfo)request.getSession().getAttribute("user");
//        如果是非法操作，则抛出异常，跳转到错误页面
        if (!user.getUserId().equals(nowUser.getUserId()) && !nowUser.getUserType().equals(3)){
            throw new ArticleException("非法操作，请不要修改他人文章");
        }

//        页面初始化所需数据
        List<Area> areaList = new ArrayList<>();
        List<ItemCategory> itemCategoryList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();
        List<ArticleCategory> articleCategoryList = new ArrayList<>();
        areaList = areaService.getAreaList();
        itemCategoryList = itemCategoryService.getItemCategoryist();
        tagList = tagMapper.allTag(1);
        articleCategoryList = articleCategoryService.getArticleCategories(1);
//       将需要下拉列表选项的数据放进去model
        model.addAttribute("areaList",areaList);
        model.addAttribute("itemCategoryList",itemCategoryList);
        model.addAttribute("tagList",tagList);
        model.addAttribute("articleCategoryList",articleCategoryList);

        return "/lost/lost-input";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute("article") LostArticle article,
                                     HttpServletRequest request,
                                     RedirectAttributes attributes){
        //从session获取用户信息
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        // 根据用户类型，判断返回位置
        String redirectUrl = new String();
        if (user.getUserType()==3){
            redirectUrl = "redirect:/admin/article";
        }else {
            redirectUrl = "redirect:/people/"+user.getUserId()+"/1";
        }

        MultipartFile articleImg = article.getUpload();

        if (article.getId()!= null) {
//            从session获取用户数据，用于前台js判断当前是否为管理员操作

            ArticleEnum le;
            try {
                if (articleImg.isEmpty()){
                    le=lostArticleService.updateArticle(article,null);
                }else {
                    //获取文件名和文件流，并作为封装到自定义类imageHolder里面。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
                    //调用service添加帖子信息和图片信息
                      le = lostArticleService.updateArticle(article,imageHolder);
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
    public String lostArticleList(@RequestParam(name = "articleId") Integer articleId,
                                  RedirectAttributes attributes,
                                  HttpServletRequest request){
        LostArticle article = lostArticleMapper.findArticleById(articleId);
//        文章作者
        UserInfo user = article.getUserInfo();
//        当前用户
        UserInfo nowUser = (UserInfo)request.getSession().getAttribute("user");
//        如果当前用户与文章作者相等，则为合法操作
        if (user.getUserId().equals(nowUser.getUserId())){
            Integer userId = user.getUserId();
//        userId用于删除本地存储图片和所在文件夹
            try {
                lostArticleService.deleteArticle(articleId,userId);
            }catch (ArticleException e){
                System.out.println(e.getMessage());
            }
            attributes.addFlashAttribute("message", "删除成功");
            return "redirect:/people/"+userId+"/1";
        }else if (nowUser.getUserType().equals(3)){
//          如果是管理员权限，也可进行操作
            Integer userId = user.getUserId();
//        userId用于删除本地存储图片和所在文件夹
            try {
                lostArticleService.deleteArticle(articleId,userId);
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
        LostArticle lostArticle = lostArticleMapper.findArticleById(articleId);
        model.addAttribute("article",lostArticle);
        lostArticleMapper.incReadCount(lostArticle);
        return "/lost/article-detail";
    }



    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


}
