package com.hzu.community.HandlerInterceptor;

import com.hzu.community.bean.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CompanyHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo loginUser = (UserInfo)request.getSession().getAttribute("user");
        if(!loginUser.getUserType().equals(2) && !loginUser.getUserType().equals(3)){
            //不是企业,且没有管理员权限,则退出账号，重新登陆
            request.getSession().removeAttribute("user");
            request.setAttribute("message","没有企业权限,请先登陆");
            Cookie cookie = new Cookie("token", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            //返回登陆页面
            request.getRequestDispatcher("/login").forward(request,response);
            return false;
        }else{
            //管理员权限，放行请求
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
