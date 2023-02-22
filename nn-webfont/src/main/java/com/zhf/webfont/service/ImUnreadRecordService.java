package com.zhf.webfont.service;

import com.zhf.webfont.po.ImUnreadRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 10276
* @description 针对表【wf_im_unread_record】的数据库操作Service
* @createDate 2023-02-19 14:24:34
*/
public interface ImUnreadRecordService extends IService<ImUnreadRecord> {

    /**
     * 添加未读消息
     * @param uuid
     * @param toUserId
     */
    void incrUnreadCount(String uuid, String toUserId);
}
