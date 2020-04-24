package com.hzu.community.HandlerInterceptor;

import com.hzu.community.bean.*;
import com.hzu.community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class CountHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private LostArticleService lostArticleService;
    @Autowired
    private SecondArticleService secondArticleService;
    @Autowired
    private HelpArticleService helpArticleService;
    @Autowired
    private JobArticleService jobArticleService;
    @Autowired
    private SchoolArticleService schoolArticleService;
    @Autowired
    private CompanyArticleService companyArticleService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Integer count1 = lostArticleService.searchCount(new LostArticle(),null);
        Integer count2 = secondArticleService.searchCount(new SecondArticle(),null,null,null);
        Integer count3 = helpArticleService.searchCount(new HelpArticle(),null);
        Integer count4 = jobArticleService.searchCount(new JobArticle(),null);
        Integer count5 = schoolArticleService.searchCount(new SchoolArticle(),null);
        Integer count6 = companyArticleService.searchCount(new CompanyArticle(),null);

        session.setAttribute("count1", count1);
        session.setAttribute("count2", count2);
        session.setAttribute("count3", count3);
        session.setAttribute("count4", count4);
        session.setAttribute("count5", count5);
        session.setAttribute("count6", count6);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
