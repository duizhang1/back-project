package com.zhf.webfont.mapper;

import com.zhf.webfont.po.ImUnreadRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 10276
* @description 针对表【wf_im_unread_record】的数据库操作Mapper
* @createDate 2023-02-19 14:24:34
* @Entity com.zhf.webfont.po.ImUnreadRecord
*/
public interface ImUnreadRecordMapper extends BaseMapper<ImUnreadRecord> {

    /**
     * 获得聊天室列表
     * @param userId
     * @return
     */
    List<ImUnreadRecord> getChatList(@Param("userId") String userId);
}




