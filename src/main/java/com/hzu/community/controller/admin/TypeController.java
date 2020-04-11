package com.hzu.community.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @GetMapping("/types")
    public String type(
            @RequestParam(name = "type",defaultValue = "1") Integer type,
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            Model model){
        PageHelper.startPage(page,5);
        List<ArticleCategory> articleCategoryList = articleCategoryService.getArticleCategories(type);

        PageInfo<ArticleCategory> pageInfo = new PageInfo<>(articleCategoryList);

        model.addAttribute("pageInfo",pageInfo);

//        回显
        model.addAttribute("typeCondition",type);
        return "/admin/types";
    }
    @GetMapping("/types/input")
    public String addTypePage(Model model){
        List<ArticleCategory> parCategoryList = articleCategoryService.getArticleCategories(null);
        model.addAttribute("parCategoryList",parCategoryList);
        model.addAttribute("ArticleCategory",new ArticleCategory());
        return "/admin/types-input";

    }
    @GetMapping("/types/update/{id}")
    public String updateTypePage(Model model,@PathVariable Integer id){
        List<ArticleCategory> parCategoryList = articleCategoryService.getArticleCategories(null);
        model.addAttribute("parCategoryList",parCategoryList);
        ArticleCategory articleCategory = articleCategoryService.findArticleCategoryById(id);
        model.addAttribute("ArticleCategory",articleCategory);
        return "/admin/types-input";
    }
    @PostMapping("/types/input")
    public String addType(@ModelAttribute("articleCategory") ArticleCategory articleCategory, RedirectAttributes attributes){
//        根据名字，去当前类别寻找有没有重复名字
        ArticleCategory articleCategory1 = articleCategoryService.findByName(articleCategory);
        if (articleCategory1 != null){
            attributes.addFlashAttribute("message", "新增失败,请不要重复增加类别");
        }else {
            int num = articleCategoryService.add(articleCategory);
            if (num>0){
                attributes.addFlashAttribute("message", "新增成功");
            }else {
                attributes.addFlashAttribute("message", "新增失败");
            }
        }
        return "redirect:/admin/types";
    }
    @PostMapping("/types/update/{id}")
    public String updateType(@ModelAttribute("articleCategory") ArticleCategory articleCategory,
                             RedirectAttributes attributes){

        ArticleCategory articleCategory1 = articleCategoryService.findByName(articleCategory);
        if (articleCategory1 != null){
            attributes.addFlashAttribute("message", "修改失败,类别名字重复");
        }else {
            int num = articleCategoryService.update(articleCategory);
            if (num>0){
                attributes.addFlashAttribute("message", "修改成功");
            }else {
                attributes.addFlashAttribute("message", "修改失败");
            }
        }
        return "redirect:/admin/types";

    }

    @GetMapping("/types/delete/{id}")
    public String delete(@PathVariable Integer id,RedirectAttributes attributes) {
        try {
            articleCategoryService.del(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }


}
