package com.hzu.community.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.NavExecution;
import com.hzu.community.enums.NavEnum;
import com.hzu.community.exceptions.NavException;
import com.hzu.community.service.impl.NavServiceImpl;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class NavDefaultController {
    @Autowired
    private NavServiceImpl navService;
    @GetMapping("/nav")
    public String nav(@RequestParam(name = "page",defaultValue = "1") Integer page,
                       Model model){
        PageHelper.startPage(page,10);
        List<Nav> navList = navService.navDefault();
        PageInfo<Nav> pageInfo = new PageInfo<>(navList);
        model.addAttribute("pageInfo",pageInfo);

        return "admin/nav";
    }
    @GetMapping("/nav/input")
    public String addNavPage(Model model){

        model.addAttribute("nav",new Nav());
        return "/admin/nav-input";

    }
    @GetMapping("/nav/update/{id}")
    public String updatePage(Model model,@PathVariable Long id
                                 ){
        Nav nav = new Nav();
        nav.setId(id);
        model.addAttribute("nav",navService.findDefaultById(nav));

        return "/admin/nav-input";
    }
    @PostMapping("/nav/input")
    public String addNav(@ModelAttribute("nav") Nav nav, RedirectAttributes attributes){
        MultipartFile navImg = nav.getUpload();
//        新增default导航
        NavExecution le;
        try {
            if (navImg.isEmpty()){
                le = navService.defaultSave(nav,null);
            }else {
                //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                ImageHolder imageHolder = new ImageHolder(navImg.getOriginalFilename(),navImg.getInputStream());
//               调用service添加帖子信息和图片信息
                le = navService.defaultSave(nav,imageHolder);
            }


            if(le.getState() == NavEnum.SUCCESS.getState()){
                attributes.addFlashAttribute("message", "新增成功");
            }else{
                attributes.addFlashAttribute("message", "新增失败"+le.getStateInfo());
            }
        }catch(NavException e){
//               处理service层抛出的异常
            attributes.addFlashAttribute("message", "新增失败"+e.getMessage());
        }catch(IOException e){
            attributes.addFlashAttribute("message", "新增失败"+e.getMessage());
        }
        return "redirect:/admin/nav";
    }

    @PostMapping("/nav/update/{id}")
    public String updateNav(@ModelAttribute("nav") Nav nav, RedirectAttributes attributes){
        MultipartFile navImg = nav.getUpload();
//        新增default导航
        NavExecution le;
        try {
            if (navImg.isEmpty()){
                le = navService.defaultUpdate(nav,null);
            }else {
                //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                ImageHolder imageHolder = new ImageHolder(navImg.getOriginalFilename(),navImg.getInputStream());
//               调用service添加帖子信息和图片信息
                le = navService.defaultUpdate(nav,imageHolder);
            }


            if(le.getState() == NavEnum.SUCCESS.getState()){
                attributes.addFlashAttribute("message", "修改成功");
            }else{
                attributes.addFlashAttribute("message", "修改失败"+le.getStateInfo());
            }
        }catch(NavException e){
//               处理service层抛出的异常
            attributes.addFlashAttribute("message", "修改失败"+e.getMessage());
        }catch(IOException e){
            attributes.addFlashAttribute("message", "修改失败"+e.getMessage());
        }
        return "redirect:/admin/nav";
    }

    @GetMapping("/nav/delete/{id}")
    public String delete(@PathVariable Integer id,RedirectAttributes attributes
                         ) {
        try {
            navService.delDefault(id);
            attributes.addFlashAttribute("message", "删除成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
            attributes.addFlashAttribute("message", "删除失败");
        }

        return "redirect:/admin/nav";
    }
}
