package com.hzu.community.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.dto.SearchDto;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.mapper.*;
import com.hzu.community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ArticleController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private LostArticleService lostArticleService;
    @Autowired
    private SecondArticleService secondArticleService;
    @Autowired
    private JobArticleService jobArticleService;
    @Autowired
    private HelpArticleService helpArticleService;
    @Autowired
    private SchoolArticleService schoolArticleService;
    @Autowired
    private CompanyArticleService companyArticleService;


    @GetMapping("/article")
    public String article(@ModelAttribute("searchDto") SearchDto searchDto,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          HttpServletRequest request,
                          Model model){
        if (searchDto.getLookMe()!=null){
            UserInfo user = (UserInfo) request.getSession().getAttribute("user");
            searchDto.setUserInfo(user);
        }
        List<ArticleCategory> categoryList = articleCategoryService.categoryList();
        PageHelper.startPage(page,10);
        List<SearchDto> searchDtoList = searchService.getAll(searchDto);
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
        ArticleEnum articleEnum ;

        if (id.equals(1)){
            LostArticle article = new LostArticle();
            article.setId(articleId);
            article.setTop(true);
            articleEnum = lostArticleService.updateArticle(article,null);
        }else if (id.equals(2)){
            SecondArticle article = new SecondArticle();
            article.setId(articleId);
            article.setTop(true);
            articleEnum = secondArticleService.updateArticle(article,null);
        }else if (id.equals(3)){
            HelpArticle article = new HelpArticle();
            article.setId(articleId);
            article.setTop(true);
            articleEnum = helpArticleService.updateArticle(article,null);
        }else if (id.equals(4)){
            JobArticle article = new JobArticle();
            article.setId(articleId);
            article.setTop(true);
            articleEnum = jobArticleService.updateArticle(article,null);
        }else if (id.equals(5)){
            SchoolArticle article = new SchoolArticle();
            article.setId(articleId);
            article.setTop(true);
            articleEnum = schoolArticleService.updateArticle(article,null);
        }else if (id.equals(6)){
            CompanyArticle article = new CompanyArticle();
            article.setId(articleId);
            article.setTop(true);
            articleEnum = companyArticleService.updateArticle(article,null);
        }else {
            articleEnum = ArticleEnum.NULL_Article;
        }
        
        if (articleEnum.getState()==ArticleEnum.SUCCESS.getState()){
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
        ArticleEnum articleEnum ;

        if (id.equals(1)){
            LostArticle article = new LostArticle();
            article.setId(articleId);
            article.setTop(false);
            articleEnum = lostArticleService.updateArticle(article,null);
        }else if (id.equals(2)){
            SecondArticle article = new SecondArticle();
            article.setId(articleId);
            article.setTop(false);
            articleEnum = secondArticleService.updateArticle(article,null);
        }else if (id.equals(3)){
            HelpArticle article = new HelpArticle();
            article.setId(articleId);
            article.setTop(false);
            articleEnum = helpArticleService.updateArticle(article,null);
        }else if (id.equals(4)){
            JobArticle article = new JobArticle();
            article.setId(articleId);
            article.setTop(false);
            articleEnum = jobArticleService.updateArticle(article,null);
        }else if (id.equals(5)){
            SchoolArticle article = new SchoolArticle();
            article.setId(articleId);
            article.setTop(false);
            articleEnum = schoolArticleService.updateArticle(article,null);
        }else if (id.equals(6)){
            CompanyArticle article = new CompanyArticle();
            article.setId(articleId);
            article.setTop(false);
            articleEnum = companyArticleService.updateArticle(article,null);
        }else {
            articleEnum = ArticleEnum.NULL_Article;
        }

        if (articleEnum.getState()==ArticleEnum.SUCCESS.getState()){
            attributes.addFlashAttribute("message", "取消置顶成功");
        }else {
            attributes.addFlashAttribute("message", "取消置顶失败");
        }
        return "redirect:/admin/article";

    }

}
