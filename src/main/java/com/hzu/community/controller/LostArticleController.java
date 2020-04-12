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
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    public String toAddPage(Map<String,Object> modelMap){
        List<Tag> tagList = new ArrayList<>();
        List<Area> areaList = new ArrayList<>();
        List<ItemCategory> itemCategoryList = new ArrayList<>();
        List<ArticleCategory> articleCategoryList = new ArrayList<>();
        try {
            areaList = areaService.getAreaList();
            itemCategoryList = itemCategoryService.getItemCategoryist();
            articleCategoryList = articleCategoryService.getArticleCategories(1);
            tagList = tagMapper.allTag(1);
//            将需要初始化的数据放进去map
            modelMap.put("areaList",areaList);
            modelMap.put("itemCategoryList",itemCategoryList);
            modelMap.put("tagList",tagList);
            modelMap.put("articleCategoryList",articleCategoryList);
            modelMap.put("init",true);
        }catch (Exception e){
            modelMap.put("init",false);
            modelMap.put("msg",e.getMessage());
        }
//        返回视图
        return  "/lost/lost-input";
    }


//    失物招领新增页面数据处理,并加入数据库
    @RequestMapping(value="/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> add(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();


        ObjectMapper mapper = new ObjectMapper();
        LostArticle lostArticle = null;
        String lostArticleStr = HttpServletRequestUtil.getString(request, "lostArticleStr");

        try {

            lostArticle = mapper.readValue(lostArticleStr, LostArticle.class);

            //将页面提交的lostArticle信息传入
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
            articleImg = (CommonsMultipartFile) MultipartHttpServletRequest.getFile("lostArticleImg");
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","无法获取图片文件流");
            return modelMap;
        }

//        新增失物招领
        if (lostArticle != null && articleImg != null){
//            从session获取数据
            UserInfo owner = (UserInfo) request.getSession().getAttribute("user");
            lostArticle.setUserInfo(owner);
            ArticleExecution le;
            try {
                //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
//               调用service添加帖子信息和图片信息
                le = lostArticleService.saveArticle(lostArticle,imageHolder);

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
        LostArticle lostArticle = lostArticleMapper.findArticleById(articleId);
        model.addAttribute("lostArticle",lostArticle);
//        判断是否为非法操作
        UserInfo user = lostArticle.getUserInfo();
        UserInfo nowUser = (UserInfo)request.getSession().getAttribute("user");
//        如果是非法操作，则重定向到当前用户界面
        if (!user.getUserId().equals(nowUser.getUserId()) && !nowUser.getUserType().equals(3)){
            return "redirect:/people/"+nowUser.getUserId()+"/1";
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

        return "/lost/lost-update";
    }
    @PostMapping("/update")
    @ResponseBody
    public Map<String,Object> update(HttpServletRequest request){
        Map<String,Object>modelMap = new HashMap<String,Object>();
        ObjectMapper mapper = new ObjectMapper();
        LostArticle lostArticle = null;
        String lostArticleStr = HttpServletRequestUtil.getString(request, "lostArticleStr");
        try {
            lostArticle = mapper.readValue(lostArticleStr, LostArticle.class);
            //将页面提交的lostArticle信息传入
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
            articleImg = (CommonsMultipartFile) MultipartHttpServletRequest.getFile("lostArticleImg");
            //        更新操作中，图片不是必须，所以不做else处理
        }

        if (lostArticle != null && lostArticle.getId()!= null) {
//            从session获取用户数据，用于前台js判断当前是否为管理员操作
            UserInfo user = (UserInfo) request.getSession().getAttribute("user");
            modelMap.put("user",user);
            ArticleExecution le;
            try {
                if (articleImg == null){
                    le=lostArticleService.updateArticle(lostArticle,null);
                }else {
                    //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
                    //调用service添加帖子信息和图片信息
                      le = lostArticleService.updateArticle(lostArticle,imageHolder);
                }

                if(le.getState() == ArticleEnum.SUCCESS.getState()){
                    modelMap.put("success",true);


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
//            非法操作，不进行操作，返回用户界面
            return "redirect:/people/"+nowUser.getUserId()+"/1";
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





}
