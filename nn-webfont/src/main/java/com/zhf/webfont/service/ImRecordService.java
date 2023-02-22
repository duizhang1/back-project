package com.zhf.webfont.service;

import com.zhf.webfont.bo.ImRecordItemParam;
import com.zhf.webfont.mapper.ImRecordMapper;
import com.zhf.webfont.po.ImRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 10276
* @description 针对表【wf_im_record】的数据库操作Service
* @createDate 2023-02-17 20:27:05
*/
public interface ImRecordService extends IService<ImRecord> {

    /**
     * 获得聊天对象列表
     * @return
     */
    List<ImRecordItemParam> getChatList();

    /**
     * 获得当前登陆用户与某个人的聊天
     * @param toUserId
     * @return
     */
    ImRecordItemParam getOneChat(String toUserId);

    /**
     * 获得与某人的聊天记录
     * @param toUserId
     * @return
     */
    List<ImRecordItemParam> getChatContent(String toUserId);
}
