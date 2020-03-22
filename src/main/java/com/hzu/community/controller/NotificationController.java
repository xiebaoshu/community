package com.hzu.community.controller;

import com.hzu.community.bean.Notification;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
//    更改通知信息的未读状态
    @GetMapping("/notification/{peopleId}/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id,
                          @PathVariable(name = "peopleId") Integer peopleId){
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        Notification notification = notificationService.getById(id);
        if (user == null) {
            return "redirect:/";
        }else if (user.getUserId()==peopleId){
//            如果是当前用户的信息通知点击，则修改未读状态
            notification.setStatus(true);
            int num = notificationService.update(notification);
            if (num>=0){
                return "redirect:/lost/" + notification.getArticleId();
            }else {
                return "redirect:/";
            }
        }else {
//            他人用户的点击，直接跳转
            return "redirect:/lost/" + notification.getArticleId();
        }



    }
}
