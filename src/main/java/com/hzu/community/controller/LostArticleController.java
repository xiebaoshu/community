package com.hzu.community.controller;
import com.hzu.community.bean.Area;
import com.hzu.community.bean.ItemCategory;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.LostArticleExecution;
import com.hzu.community.enums.LostArticleEnum;
import com.hzu.community.exceptions.LostArticleException;
import com.hzu.community.service.AreaService;
import com.hzu.community.service.ItemCategoryService;
import com.hzu.community.service.LostArticleService;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lostArticle")
public class LostArticleController {
    @Autowired
    AreaService areaService;
    @Autowired
    ItemCategoryService itemCategoryService;

    @Autowired
    LostArticleService lostArticleService;

//    获取初始化信息
    @GetMapping("/add")
    public String toAddPage(Map<String,Object> modelMap){

        List<Area> areaList = new ArrayList<>();
        List<ItemCategory> itemCategoryList = new ArrayList<>();
        try {
            areaList = areaService.getAreaList();
            itemCategoryList = itemCategoryService.getItemCategoryist();
//            将需要初始化的数据放进去map
            modelMap.put("areaList",areaList);
            modelMap.put("itemCategoryList",itemCategoryList);
            modelMap.put("init",true);
        }catch (Exception e){
            modelMap.put("init",false);
            modelMap.put("msg",e.getMessage());
        }
//        返回视图
        return  "lost-input";
    }


    @PostMapping("/add")
    public String add(HttpServletRequest request,RedirectAttributes attributes){

        UserInfo user = new UserInfo();
        user.setUserId(1);
        user.setUserName("谢豪");
        request.getSession().setAttribute("user", user);

        ObjectMapper mapper = new ObjectMapper();
        LostArticle lostArticle = null;
        String lostArticleStr = HttpServletRequestUtil.getString(request, "lostArticleStr");

       try {

           lostArticle = mapper.readValue(lostArticleStr, LostArticle.class);
           System.out.println(lostArticle);
           //将页面提交的lostArticle信息传入
       } catch (Exception e){
           System.out.println(e.getMessage());
       }


        CommonsMultipartFile articleImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //获取上传的图片文件，需要pom.xml配置commons-fileupload，需在springweb配置文件上传解析器multipartResolver
        if(commonsMultipartResolver.isMultipart(request)){
            System.out.println(commonsMultipartResolver.isMultipart(request));
            MultipartHttpServletRequest MultipartHttpServletRequest = (MultipartHttpServletRequest) request;
            articleImg = (CommonsMultipartFile) MultipartHttpServletRequest.getFile("lostArticleImg");
        }else{
           /* attributes.addFlashAttribute("insert", "提交失败");
            attributes.addFlashAttribute("errMsg", "上传图片不能为空");
            return "redirect:/success";*/
            System.out.println("上传图片不能为空");
        }

//        新增失物招领
        if (lostArticle !=null && articleImg !=null){
            UserInfo owner = (UserInfo) request.getSession().getAttribute("user");
            lostArticle.setUserInfo(owner);
            LostArticleExecution le;
           try {
               //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
               ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
//               调用service添加帖子信息和图片信息
               le = lostArticleService.saveArticle(lostArticle,imageHolder);
               System.out.println(le.getState());
               System.out.println(LostArticleEnum.SUCCESS.getState());
               if(le.getState() == LostArticleEnum.SUCCESS.getState()){
                   attributes.addFlashAttribute("insert", "提交成功");
                   System.out.println(le.getState());
                   System.out.println(LostArticleEnum.SUCCESS.getState());
               }else{
                   attributes.addFlashAttribute("insert", "提交失败");
                   attributes.addFlashAttribute("errMsg", "execeution错误");
               }
           }catch(LostArticleException e){
//               处理service层抛出的异常
               attributes.addFlashAttribute("insert", "提交失败");
               attributes.addFlashAttribute("errMsg", "service抛出错误");
           }catch(IOException e){
               attributes.addFlashAttribute("insert", "提交失败");
               attributes.addFlashAttribute("errMsg", "IOException");
           }
            return "redirect:/success";


        }else {
            if (articleImg ==null){
                attributes.addFlashAttribute("insert", "提交失败");
                attributes.addFlashAttribute("errMsg", "后台获取图片失败");
                return "redirect:/success";
            }else {
                attributes.addFlashAttribute("insert", "提交失败");
                attributes.addFlashAttribute("errMsg", "后台获取帖子信息失败");
                return "redirect:/success";
            }

        }

    }



}
