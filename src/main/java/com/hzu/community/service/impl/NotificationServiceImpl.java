package com.hzu.community.service.impl;

import com.hzu.community.bean.Notification;
import com.hzu.community.mapper.NotificationMapper;
import com.hzu.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Override
    public int add(Notification notification) {
        return notificationMapper.addNotification(notification);
    }

    @Override
    public List<Notification> getList(Integer userId) {
        return notificationMapper.notificationList(userId);
    }

    @Override
    public int update(Notification notification) {
        return notificationMapper.updateNotification(notification);
    }

    @Override
    public Notification getById(Long id) {
        return notificationMapper.getNotificationById(id);
    }

    @Override
    public Long unreadCount(Integer userId) {
        return notificationMapper.countUnread(userId);
    }
}
