package com.zhf.webfont.service;

import com.zhf.webfont.po.NotificationUnread;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 10276
* @description 针对表【wf_notification_unread】的数据库操作Service
* @createDate 2023-02-23 20:39:37
*/
public interface NotificationUnreadService extends IService<NotificationUnread> {

    /**
     * 清除当前登陆用户未读的点赞消息
     */
    void clearLikeUnreadCount();

    /**
     * 向创作者添加一个未读的数量
     * @param creatorId
     */
    void addLikeUnreadCount(String creatorId);
}
