package com.zhf.webfont.service;

import com.zhf.common.bo.BasePageParam;
import com.zhf.webfont.po.UserSubscribe;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 10276
* @description 针对表【wf_user_subscribe】的数据库操作Service
* @createDate 2023-02-03 01:32:31
*/
public interface UserSubscribeService extends IService<UserSubscribe> {

    /**
     * 关注用户
     * @param userId
     */
    void subscribeUser(String userId);

    /**
     * 取关用户
     * @param userId
     */
    void unSubscribeUser(String userId);

    /**
     * 获得当前用户的关注
     * @param userId
     * @return
     */
    UserSubscribe getNowUserSubscribe(String userId);

    /**
     * 获得用户关注消息通知
     * @param pageParam
     * @return
     */
    Map<String, Object> getUserSubscribeNotification(BasePageParam pageParam);
}
