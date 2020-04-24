package com.hzu.community.HandlerInterceptor;

import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.SearchDto;
import com.hzu.community.mapper.UserInfoMapper;
import com.hzu.community.service.NotificationService;
import com.hzu.community.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SearchService searchService;
//    拦截器service调用注入，需要在webmvcconfig，先注入拦截器service，否则无法取出mapper
    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        通过cookies设置session的user
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {

                    String token = cookie.getValue();
                    UserInfo user = userInfoMapper.findUserByToken(token);
                    if (user != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);
//                        将部分公用数据保存在session
                        Long unreadCount = notificationService.unreadCount(user.getUserId());
                        session.setAttribute("unreadCount", unreadCount);
//                        最热资讯前三
                        SearchDto hot = new SearchDto();
                        hot.setSort("read_count");
                        List<SearchDto> topHot = searchService.getTOP3(hot);
                        session.setAttribute("topHot", topHot);
                        //最新资讯前三
                        SearchDto time = new SearchDto();
                        time.setSort("edit_time");
                        List<SearchDto> topTime = searchService.getTOP3(time);
                        session.setAttribute("topTime", topTime);

                    }
                    break;

                }
           }
         }

        Object loginUser = request.getSession().getAttribute("user");
        if(loginUser == null){
            //未登陆
            request.setAttribute("message","没有权限,请先登陆");
            //返回登陆页面
            request.getRequestDispatcher("/login").forward(request,response);
            return false;
        }else{
            //已登陆，放行请求
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
