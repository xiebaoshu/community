package com.hzu.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;

import com.hzu.community.dto.NavExecution;
import com.hzu.community.dto.NavExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.enums.NavEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.exceptions.NavException;
import com.hzu.community.mapper.UserInfoMapper;
import com.hzu.community.service.NavService;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.naming.Name;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NavController {
    @Autowired
    private NavService navService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @GetMapping("/")
    public String navPage(Model model,HttpServletRequest request){
//        初始化设置区域 默认列表
        List<Nav> navList = new ArrayList<>();
        navList = navService.navDefault();
        model.addAttribute("navDefault",navList);
        return "nav/nav.html";
    }

//    返回导航区域
    @GetMapping("/nav")
    public String nav(HttpServletRequest request, Model model,
                      @RequestParam(name = "page", defaultValue = "1") Integer page){
//        判断session是否存在user，不存在的话，返回默认提供的导航列表

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        if (user==null){
            PageHelper.startPage(page,8);
            List<Nav> navList = new ArrayList<>();
            navList = navService.navDefault();
            PageInfo<Nav> pageInfo  = new PageInfo<>(navList);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("showType",1);
        }else {
            if (user.getNavType()==1){
                PageHelper.startPage(page,8);
            }else {
                PageHelper.startPage(page,10);
            }
            List<Nav> navList = new ArrayList<>();
            navList = navService.navDiy(user);
            PageInfo<Nav> pageInfo  = new PageInfo<>(navList);
            model.addAttribute("pageInfo",pageInfo);
            model.addAttribute("showType",user.getNavType());

        }

        return "fragments :: nav_area";
    }
//    返回我的添加区域
    @GetMapping("/nav/mySet")
    public String mySet(Model model,HttpServletRequest request){
        //        根据user获取设置区域 我的添加列表
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("hasUser",false);
        }else {
            List<Nav> navDiy = new ArrayList<>();
            navDiy = navService.navDiy(user);
            model.addAttribute("navDiy",navDiy);
            model.addAttribute("hasUser",true);
        }
        return "fragments :: my_set";
    }


//    从默认列表添加导航
    @PostMapping("/nav/add")
    @ResponseBody
    public Map<String,Object> add(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Nav nav = null;
        String navStr = HttpServletRequestUtil.getString(request,"navStr");
        try {
           nav = objectMapper.readValue(navStr,Nav.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        nav.setUserId(user.getUserId());
//        该查询需要userID和name作为查询条件
        Nav nav1 =navService.findByName(nav);
        if (nav1!=null){
            modelMap.put("success",false);
            modelMap.put("errMsg","该导航已存在，请不要重复添加");
            return modelMap;
        }
        int num = navService.add(nav);
        if (num>0){
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","数据库操作失败");
            return modelMap;
        }
        return modelMap;

    }


    @PostMapping("/nav/delete")
    @ResponseBody
    public Map<String,Object> delete(HttpServletRequest request,
                                      @RequestParam(name = "id") Integer id){
        Map<String,Object> modelMap = new HashMap<>();
        try {
            navService.del(id);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;

    }


    @PostMapping("/navShow")
    @ResponseBody
    public Map<String,Object> navShow(HttpServletRequest request,
                                      @RequestParam(name = "showType") Integer showType){
        Map<String,Object> modelMap = new HashMap<>();
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        user.setNavType(showType);
        try {
            userInfoMapper.update(user);
            user = userInfoMapper.findUserInfoById(user.getUserId());
            request.getSession().removeAttribute("user");
            request.getSession().setAttribute("user",user);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;
        
    }
    //自定义导航新增数据处理,并加入数据库
    @RequestMapping(value="/nav/diy",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> diy(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Nav nav = null;
        String navStr = HttpServletRequestUtil.getString(request, "navStr");
        try {
            nav = mapper.readValue(navStr, Nav.class);
            //将页面提交的nav信息传入
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile navImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //获取上传的图片文件，需要pom.xml配置commons-fileupload，需在springweb配置文件上传解析器multipartResolver
        if(commonsMultipartResolver.isMultipart(request)){

            MultipartHttpServletRequest MultipartHttpServletRequest = (MultipartHttpServletRequest) request;
            navImg = (CommonsMultipartFile) MultipartHttpServletRequest.getFile("navImg");
        }

//        新增diy导航
//        从session获取数据
            UserInfo user = (UserInfo) request.getSession().getAttribute("user");
            nav.setUserId(user.getUserId());
            NavExecution le;
            try {
                if (navImg == null){
                    le = navService.diySave(nav,null);
                }else {
                    //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                    ImageHolder imageHolder = new ImageHolder(navImg.getOriginalFilename(),navImg.getInputStream());
//               调用service添加帖子信息和图片信息
                    le = navService.diySave(nav,imageHolder);
                }


                if(le.getState() == NavEnum.SUCCESS.getState()){
                    modelMap.put("success",true);

                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",le.getStateInfo());
                    return modelMap;
                }
            }catch(NavException e){
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




    }
}
