package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.common.util.TransactionUtil;
import com.zhf.webfont.po.NotificationUnread;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.NotificationUnreadService;
import com.zhf.webfont.mapper.NotificationUnreadMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
* @author 10276
* @description 针对表【wf_notification_unread】的数据库操作Service实现
* @createDate 2023-02-23 20:39:37
*/
@Service
public class NotificationUnreadServiceImpl extends ServiceImpl<NotificationUnreadMapper, NotificationUnread>
    implements NotificationUnreadService{

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private NotificationUnreadMapper notificationUnreadMapper;

    @Override
    public void clearLikeUnreadCount() {
        User curUser = jwtTokenUtil.getCurrentUserFromHeader();
        NotificationUnread notificationUnread = getNotificationUnreadByUserId(curUser.getUuid());
        if(notificationUnread != null){
            notificationUnread.setLikeCount(0);
            notificationUnread.setUpdateTime(new Date());
            notificationUnreadMapper.updateById(notificationUnread);
        }
    }

    @Override
    public void addLikeUnreadCount(String creatorId) {
        TransactionUtil.transaction(()->{
            NotificationUnread notificationUnread = getNotificationUnreadByUserId(creatorId);
            notificationUnread.setLikeCount(notificationUnread.getLikeCount()+1);
            notificationUnreadMapper.updateById(notificationUnread);
        });
    }

    @Override
    public void clearFocusUnreadCount() {
        User curUser = jwtTokenUtil.getCurrentUserFromHeader();
        NotificationUnread notificationUnread = getNotificationUnreadByUserId(curUser.getUuid());
        if (notificationUnread != null && !notificationUnread.getFocusCount().equals(0)){
            notificationUnread.setFocusCount(0);
            notificationUnread.setUpdateTime(new Date());
            notificationUnreadMapper.updateById(notificationUnread);
        }
    }

    @Override
    public void addFocusUnreadCount(String userId) {
        TransactionUtil.transaction(()->{
            NotificationUnread notificationUnread = getNotificationUnreadByUserId(userId);
            notificationUnread.setFocusCount(notificationUnread.getFocusCount()+1);
            notificationUnreadMapper.updateById(notificationUnread);
        });
    }

    private NotificationUnread getNotificationUnreadByUserId(String userId) {
        QueryWrapper<NotificationUnread> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return notificationUnreadMapper.selectOne(wrapper);
    }
}




