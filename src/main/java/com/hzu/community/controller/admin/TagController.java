package com.hzu.community.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.Tag;
import com.hzu.community.mapper.ArticleCategoryMapper;
import com.hzu.community.service.ArticleCategoryService;
import com.hzu.community.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Name;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @GetMapping("/tags")
    public String tag(@RequestParam(name = "type",defaultValue = "1") Integer type,
//                      type参数为文章模块类型
                      @RequestParam(name = "parTag",required = false) Integer parTag,
                      @RequestParam(name = "page",defaultValue = "1") Integer page,
                      Model model){
        //        获取一级标签
        List<Tag> firstTagList =  tagService.allTag(type);
        model.addAttribute("firstTagList",firstTagList);
        //        如果没有传递选中的一级标签参数，将第一个一级标签作为参数，默认选中
        if (parTag==null){
            parTag = firstTagList.get(0).getId();
        }
        PageHelper.startPage(page,5);
//       获取二级标签
        List<Tag> secondTagList = tagService.tagList(parTag);
        PageInfo<Tag> pageInfo = new PageInfo<>(secondTagList);
        model.addAttribute("pageInfo",pageInfo);

        //        回显
        model.addAttribute("parTagCondition",parTag);
        model.addAttribute("typeCondition",type);
        return "/admin/tags";
    }
    @GetMapping("/tags/input")
    public String add(@RequestParam(name = "type",defaultValue = "1") Integer type,
//                      type=1为添加一级标签，2为2级标签
                      Model model){
        if (type.equals(1)){
            List<ArticleCategory> articleCategoryList = articleCategoryService.getArticleCategories(null);
            model.addAttribute("articleCategoryList",articleCategoryList);
        }else {
            List<ArticleCategory> articleTags = articleCategoryMapper.articleTags();
            model.addAttribute("articleTags",articleTags);
        }
        model.addAttribute("Tag",new Tag());
        model.addAttribute("typeCondition",type);
        return "/admin/tags-input";
    }

    @GetMapping("/tags/update/{id}")
    public String updateTypePage(Model model,@PathVariable Integer id,
                                 @RequestParam(name = "type") Integer type){
        if (type.equals(1)){
            List<ArticleCategory> articleCategoryList = articleCategoryService.getArticleCategories(null);
            model.addAttribute("articleCategoryList",articleCategoryList);
        }else {
            List<ArticleCategory> articleTags = articleCategoryMapper.articleTags();
            model.addAttribute("articleTags",articleTags);
        }
        model.addAttribute("Tag",tagService.findById(id));
        model.addAttribute("typeCondition",type);
        return "/admin/tags-input";
    }

    @PostMapping("/tags/input")
    public String addType(@ModelAttribute("tag") Tag tag, RedirectAttributes attributes,
                          @RequestParam(name = "type") Integer type){
        if (type.equals(2)){
            //新增二级标签，通过父标签id，获取设置文章类别
            tag.setArticleParCategory(tagService.findById(tag.getParId()).getArticleParCategory());
        }
        Tag tag1 = tagService.findByName(tag);
        if (tag1 != null){
            attributes.addFlashAttribute("message", "新增失败,请不要重复添加标签");
        }else {
            int num = tagService.add(tag);
            if (num>0){
                attributes.addFlashAttribute("message", "新增成功");
            }else {
                attributes.addFlashAttribute("message", "新增失败");
            }
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/update/{id}")
    public String updateType(@ModelAttribute("tag") Tag tag, RedirectAttributes attributes,
                             @RequestParam(name = "type") Integer type){
        if (type.equals(2)){
            //修改二级标签，通过父标签id，获取设置文章类别
            tag.setArticleParCategory(tagService.findById(tag.getParId()).getArticleParCategory());
        }
        Tag tag1 = tagService.findByName(tag);
        if (tag1 != null){
            attributes.addFlashAttribute("message", "修改失败,标签名字重复");
        }else {
            int num = tagService.update(tag);
            if (num>0){
                attributes.addFlashAttribute("message", "修改成功");
            }else {
                attributes.addFlashAttribute("message", "修改失败");
            }
        }
        return "redirect:/admin/tags";

    }

    @GetMapping("/tags/delete/{id}")
    public String delete(@PathVariable Integer id,RedirectAttributes attributes,
                         @RequestParam(name = "type") Integer type) {
        try {
            if (type.equals(1)){
                tagService.delList(id);
            }else {
                tagService.del(id);
            }
            attributes.addFlashAttribute("message", "删除成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
            attributes.addFlashAttribute("message", "删除失败");
        }

        return "redirect:/admin/tags";
    }

}
