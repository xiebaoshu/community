package com.hzu.community.service;

import com.hzu.community.bean.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotificationService {
    public int add(Notification notification);
    public List<Notification> getList(Integer userId);
    public List<Notification> replyList(Integer userId);
    public int update(Notification notification);
    public Notification getById(Long id);
    public Long unreadCount(Integer userId);
    public int delNotification(Notification notification);
    public void batchDel(@Param("parId") Integer parId, @Param( "List" ) List  List);
}
