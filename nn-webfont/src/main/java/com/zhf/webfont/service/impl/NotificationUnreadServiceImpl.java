package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.common.exception.Asserts;
import com.zhf.common.util.TransactionUtil;
import com.zhf.webfont.po.NotificationUnread;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ImUnreadRecordService;
import com.zhf.webfont.service.NotificationUnreadService;
import com.zhf.webfont.mapper.NotificationUnreadMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @Resource
    private ImUnreadRecordService imUnreadRecordService;

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

    @Override
    public Map<String,Object> getNotificationUnreadCount() {
        User curUser = jwtTokenUtil.getCurrentUserFromHeader();
        Map<String,Object> map = new HashMap<>();
        if (curUser == null){
            map.put("uuid","");
            map.put("userId","");
            map.put("likeCount",0);
            map.put("commentCount",0);
            map.put("focusCount",0);
            map.put("imCount",0);
            map.put("createTime",new Date());
            map.put("updateTime",new Date());
            return map;
        }
        QueryWrapper<NotificationUnread> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",curUser.getUuid());
        NotificationUnread notificationUnread = notificationUnreadMapper.selectOne(wrapper);

        // 获得聊天室的未读消息数量
        Long imUnreadCount = imUnreadRecordService.getImUnreadCount();
        map.put("uuid",notificationUnread.getUuid());
        map.put("userId",notificationUnread.getUserId());
        map.put("likeCount",notificationUnread.getLikeCount());
        map.put("commentCount",notificationUnread.getCommentCount());
        map.put("focusCount",notificationUnread.getFocusCount());
        map.put("imCount",imUnreadCount);
        map.put("createTime",notificationUnread.getCreateTime());
        map.put("updateTime",notificationUnread.getUpdateTime());
        return map;
    }

    public void InsertNotificationUnreadRecord(String userId){
        // 创建消息记录的初始记录
        NotificationUnread notificationUnread = new NotificationUnread();
        notificationUnread.setLikeCount(0);
        notificationUnread.setCommentCount(0);
        notificationUnread.setFocusCount(0);
        notificationUnread.setCreateTime(new Date());
        notificationUnread.setUpdateTime(new Date());
        notificationUnread.setUserId(userId);

        int insert = notificationUnreadMapper.insert(notificationUnread);
        Asserts.failIsTrue(insert <= 0,"出错了，创建失败");
    }

    private NotificationUnread getNotificationUnreadByUserId(String userId) {
        QueryWrapper<NotificationUnread> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return notificationUnreadMapper.selectOne(wrapper);
    }
}




