package com.hzu.community.config;

import com.hzu.community.HandlerInterceptor.AdminHandlerInterceptor;
import com.hzu.community.HandlerInterceptor.CompanyHandlerInterceptor;
import com.hzu.community.HandlerInterceptor.LoginHandlerInterceptor;
import com.hzu.community.HandlerInterceptor.TeacherHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginHandlerInterceptor loginHandlerInterceptor;
    @Autowired
    private AdminHandlerInterceptor adminHandlerInterceptor;
    @Autowired
    private TeacherHandlerInterceptor teacherHandlerInterceptor;
    @Autowired
    private CompanyHandlerInterceptor companyHandlerInterceptor;


    /**
     * 视图跳转控制器
     * 无业务逻辑的跳转 均可以以这种方法写在这里
     *
     * @param registry
     */
    public void addViewControllers(ViewControllerRegistry registry) {

//       配置视图
        registry.addViewController("/success").setViewName("success");

    }
    //    注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        addPathPatterns拦截哪些路径，excludePathPatterns排除哪些路径，登陆拦截器
        registry.addInterceptor(loginHandlerInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/","/static/**","/login","/nav","/nav/mySet","/register");
//        管理员权限拦截器
        registry.addInterceptor(adminHandlerInterceptor).addPathPatterns("/admin/**")
                .excludePathPatterns("/static/**");
//        教师权限拦截器
        registry.addInterceptor(teacherHandlerInterceptor).addPathPatterns("/5/add","/5/update","/5/delete")
                .excludePathPatterns("/static/**");
        //企业权限拦截器
        registry.addInterceptor(companyHandlerInterceptor).addPathPatterns("/6/add","/6/update","/6/delete")
                .excludePathPatterns("/static/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //告知系统static 当成 静态资源访问
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");


        //配置虚拟路径，将upload替换成
        registry.addResourceHandler("/upload/**").addResourceLocations("file:E:/workspace/community/src/main/resources/static/images/upload/");
    }


    //    文件解析对象
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxInMemorySize(40960);
        //上传文件大小 50M 50*1024*1024
        resolver.setMaxUploadSize(50*1024*1024);
        return resolver;
    }


}