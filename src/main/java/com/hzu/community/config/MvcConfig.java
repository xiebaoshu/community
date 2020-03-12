package com.hzu.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class MvcConfig implements WebMvcConfigurer {


    /**
     * 视图跳转控制器
     * 无业务逻辑的跳转 均可以以这种方法写在这里
     *
     * @param registry
     */
    public void addViewControllers(ViewControllerRegistry registry) {

//        registry.addViewController("/addLost").setViewName("lost-input");
//        registry.addViewController("/add").setViewName("lost-input");
        registry.addViewController("/success").setViewName("success");

    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //告知系统static 当成 静态资源访问
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
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