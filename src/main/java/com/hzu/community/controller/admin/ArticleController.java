package com.hzu.community.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.dto.SearchDto;
import com.hzu.community.mapper.*;
import com.hzu.community.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ArticleController {
    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private LostArticleMapper lostArticleMapper;
    @Autowired
    private SecondArticleMapper secondArticleMapper;
    @Autowired
    private JobArticleMapper jobArticleMapper;
    @Autowired
    private HelpArticleMapper helpArticleMapper;
    @Autowired
    private SchoolArticleMapper schoolArticleMapper;
    @Autowired
    private CompanyArticleMapper companyArticleMapper;


    @GetMapping("/article")
    public String article(@ModelAttribute("searchDto") SearchDto searchDto,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          Model model){

        List<ArticleCategory> categoryList = articleCategoryService.categoryList();
        PageHelper.startPage(page,10);
        List<SearchDto> searchDtoList = searchMapper.getAll(searchDto);
        PageInfo<SearchDto> pageInfo = new PageInfo<>(searchDtoList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("searchDto",searchDto);


        return "admin/article";
    }
    @GetMapping("/{id}/top")
    public String top(@PathVariable("id") Integer id,
                      @RequestParam("articleId") Integer articleId,
                      RedirectAttributes attributes){
        int num = 0 ;

        if (id.equals(1)){
            LostArticle article = new LostArticle();
            article.setId(articleId);
            article.setTop(true);
            num = lostArticleMapper.updatelost(article);
        }else if (id.equals(2)){
            SecondArticle article = new SecondArticle();
            article.setId(articleId);
            article.setTop(true);
            num = secondArticleMapper.update(article);
        }else if (id.equals(3)){
            HelpArticle article = new HelpArticle();
            article.setId(articleId);
            article.setTop(true);
            num = helpArticleMapper.update(article);
        }else if (id.equals(4)){
            JobArticle article = new JobArticle();
            article.setId(articleId);
            article.setTop(true);
            num = jobArticleMapper.update(article);
        }else if (id.equals(5)){
            SchoolArticle article = new SchoolArticle();
            article.setId(articleId);
            article.setTop(true);
            num = schoolArticleMapper.update(article);
        }else if (id.equals(6)){
            CompanyArticle article = new CompanyArticle();
            article.setId(articleId);
            article.setTop(true);
            num = companyArticleMapper.update(article);
        }
        
        if (num>0){
            attributes.addFlashAttribute("message", "置顶成功");
        }else {
            attributes.addFlashAttribute("message", "置顶失败");
        }
        return "redirect:/admin/article";

    }


    @GetMapping("/{id}/untop")
    public String untop(@PathVariable("id") Integer id,
                      @RequestParam("articleId") Integer articleId,
                      RedirectAttributes attributes){
        int num = 0 ;

        if (id.equals(1)){
            LostArticle article = new LostArticle();
            article.setId(articleId);
            article.setTop(false);
            num = lostArticleMapper.updatelost(article);
        }else if (id.equals(2)){
            SecondArticle article = new SecondArticle();
            article.setId(articleId);
            article.setTop(false);
            num = secondArticleMapper.update(article);
        }else if (id.equals(3)){
            HelpArticle article = new HelpArticle();
            article.setId(articleId);
            article.setTop(false);
            num = helpArticleMapper.update(article);
        }else if (id.equals(4)){
            JobArticle article = new JobArticle();
            article.setId(articleId);
            article.setTop(false);
            num = jobArticleMapper.update(article);
        }else if (id.equals(5)){
            SchoolArticle article = new SchoolArticle();
            article.setId(articleId);
            article.setTop(false);
            num = schoolArticleMapper.update(article);
        }else if (id.equals(6)){
            CompanyArticle article = new CompanyArticle();
            article.setId(articleId);
            article.setTop(false);
            num = companyArticleMapper.update(article);
        }

        if (num>0){
            attributes.addFlashAttribute("message", "取消置顶成功");
        }else {
            attributes.addFlashAttribute("message", "取消置顶失败");
        }
        return "redirect:/admin/article";

    }

}
