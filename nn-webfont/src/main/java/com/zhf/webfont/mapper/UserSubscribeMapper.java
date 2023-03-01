package com.zhf.webfont.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhf.webfont.po.UserSubscribe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 10276
* @description 针对表【wf_user_subscribe】的数据库操作Mapper
* @createDate 2023-02-03 01:32:31
* @Entity com.zhf.webfont.po.UserSubscribe
*/
public interface UserSubscribeMapper extends BaseMapper<UserSubscribe> {

    /**
     * 获得分页的消息通知的关注数据
     * @param iPage
     * @param userId
     * @return
     */
    IPage<UserSubscribe> selectFocusPageByCurrentUser(IPage<UserSubscribe> iPage,@Param("userId") String userId);
}




