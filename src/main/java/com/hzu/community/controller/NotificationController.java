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
    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id){
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        Notification notification = notificationService.getById(id);
        notification.setStatus(true);
        int num = notificationService.update(notification);
        if (num>=0){
            return "redirect:/lost/article/" + notification.getArticleId();
        }else {
            return "redirect:/";
        }

    }
}
